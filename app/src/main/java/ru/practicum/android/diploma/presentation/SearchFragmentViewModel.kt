package ru.practicum.android.diploma.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyShort
import ru.practicum.android.diploma.util.debouncedAction
import java.net.HttpURLConnection

class SearchFragmentViewModel(
    private val vacancyInteractor: VacancyInteractor
) : ViewModel() {
    private var lastSearchedValue: String = ""
    private var lastLoadedPage: Int = 0
    private var totalPagesInLastRequest: Int = 0
    private var vacancyList = mutableListOf<VacancyShort>()

    private val screenState = MutableLiveData<SearchFragmentState>()
    private val filtersButtonState = MutableLiveData<Boolean>()
    fun observeData(): LiveData<SearchFragmentState> = screenState
    fun observeFilter(): LiveData<Boolean> = filtersButtonState

    fun addFilter() {
        val newValue = filtersButtonState.value ?: false
        filtersButtonState.postValue(!newValue)
    }

    val search: (String) -> Unit =
        debouncedAction(SEARCH_DEBOUNCE_DELAY, viewModelScope) { searchText ->
            processNewSearch(searchText)
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
            vacancyInteractor.searchVacancies(lastSearchedValue, ++lastLoadedPage).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        if (result.items.isNotEmpty()) {
                            vacancyList.addAll(result.items)
                        }
                        screenState.postValue(SearchFragmentState.ShowingResults(vacancyList, result.total))
                    }

                    is Resource.Error -> {
                        screenState.postValue(SearchFragmentState.ShowingResults(vacancyList))
                    }
                }
            }
        }
    }

    private fun processNewSearch(text: String) {
        lastLoadedPage = 0
        totalPagesInLastRequest = 0
        if (lastSearchedValue == text) {
            return
        }
        viewModelScope.launch {
            screenState.postValue(SearchFragmentState.RequestInProgress)
            vacancyList.clear()
            lastSearchedValue = text
            vacancyInteractor.searchVacancies(text, lastLoadedPage).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        if (result.items.isNotEmpty()) {
                            totalPagesInLastRequest = result.pages
                            vacancyList.addAll(result.items)
                            screenState.postValue(SearchFragmentState.ShowingResults(result.items, result.total))
                        } else {
                            screenState.postValue(SearchFragmentState.EmptyResults)
                        }
                    }

                    is Resource.Error -> {
                        when (result.code) {
                            HttpURLConnection.HTTP_BAD_REQUEST -> {
                                screenState.postValue(SearchFragmentState.ServerError)
                            }
                            HttpURLConnection.HTTP_FORBIDDEN -> {
                                screenState.postValue(SearchFragmentState.ServerError)
                            }
                            HttpURLConnection.HTTP_NOT_FOUND -> {
                                screenState.postValue(SearchFragmentState.ServerError)
                            }
                            else -> { screenState.postValue(SearchFragmentState.NoInternetAccess) }
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
