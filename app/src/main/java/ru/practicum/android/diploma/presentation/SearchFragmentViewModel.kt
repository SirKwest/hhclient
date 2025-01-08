package ru.practicum.android.diploma.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.SearchOptions
import ru.practicum.android.diploma.domain.models.VacanciesSearchResource
import ru.practicum.android.diploma.domain.models.VacancyShort
import ru.practicum.android.diploma.util.debouncedAction
import java.net.HttpURLConnection

class SearchFragmentViewModel(
    private val vacancyInteractor: VacancyInteractor,
    private val filterInteractor: FilterInteractor
) : ViewModel() {
    private var lastSearchedValue: String = ""
    private var lastLoadedPage: Int = 0
    private var totalPagesInLastRequest: Int = 0
    private var totalVacanciesFound: Int = 0
    private var filterState: Filter = Filter()
    private var vacancyList = mutableListOf<VacancyShort>()

    private var errorMessage = MutableLiveData<Int>()
    private val screenState = MutableLiveData<SearchFragmentState>()
    private val filtersButtonState = MutableLiveData<Boolean>()
    fun observeData(): LiveData<SearchFragmentState> = screenState
    fun observeFilter(): LiveData<Boolean> = filtersButtonState
    fun observeErrorMessage(): LiveData<Int> = errorMessage

    init {
        checkFilterValuesExistence()
    }

    val search: (String) -> Unit =
        debouncedAction(SEARCH_DEBOUNCE_DELAY, viewModelScope) { searchText ->
            processNewSearch(searchText)
        }

    fun performSearch(text: String) {
        if (text != lastSearchedValue) {
            search(text)
        }
    }
    fun loadNextPage() {
        if (screenState.value == SearchFragmentState.LoadingNewPageOfResults) {
            return
        }

        if (lastLoadedPage == totalPagesInLastRequest) {
            return
        }
        viewModelScope.launch {
            screenState.postValue(SearchFragmentState.LoadingNewPageOfResults)
            vacancyInteractor.searchVacancies(
                SearchOptions(text = lastSearchedValue, page = ++lastLoadedPage, filter = filterState)
            ).collect { result ->
                when (result) {
                    is VacanciesSearchResource.Success -> {
                        if (result.items.isNotEmpty()) {
                            vacancyList.addAll(result.items)
                        }
                        screenState.postValue(SearchFragmentState.ShowingResults(vacancyList, result.total))
                    }

                    is VacanciesSearchResource.Error -> {
                        screenState.postValue(SearchFragmentState.ShowingResults(vacancyList, totalVacanciesFound))
                        errorMessage.postValue(result.code)
                    }
                }
            }
        }
    }

    fun applyFiltersAndSearch() {
        checkFilterValuesExistence()
        processNewSearch(lastSearchedValue)
    }

    private fun checkFilterValuesExistence() {
        val isFiltersSaved = filterInteractor.isFiltersSaved()
        filtersButtonState.postValue(isFiltersSaved)
        if (isFiltersSaved) {
            filterState = filterInteractor.getFilter()
        }
    }

    private fun processNewSearch(text: String) {
        lastLoadedPage = 0
        totalPagesInLastRequest = 0
        viewModelScope.launch {
            screenState.postValue(SearchFragmentState.RequestInProgress)
            vacancyList.clear()
            lastSearchedValue = text
            vacancyInteractor.searchVacancies(
                SearchOptions(text = text, page = lastLoadedPage, filter = filterState)
            ).collect { result ->
                when (result) {
                    is VacanciesSearchResource.Success -> {
                        if (result.items.isNotEmpty()) {
                            totalPagesInLastRequest = result.pages
                            vacancyList.addAll(result.items)
                            totalVacanciesFound = result.total
                            screenState.postValue(SearchFragmentState.ShowingResults(result.items, result.total))
                        } else {
                            screenState.postValue(SearchFragmentState.EmptyResults)
                        }
                    }
                    is VacanciesSearchResource.Error -> {
                        when (result.code) {
                            HttpURLConnection.HTTP_BAD_REQUEST,
                            HttpURLConnection.HTTP_FORBIDDEN,
                            HttpURLConnection.HTTP_NOT_FOUND -> {
                                screenState.postValue(SearchFragmentState.ServerError)
                            }
                            else -> {
                                screenState.postValue(SearchFragmentState.NoInternetAccess)
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
