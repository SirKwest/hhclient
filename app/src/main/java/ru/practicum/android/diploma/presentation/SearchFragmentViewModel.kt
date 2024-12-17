package ru.practicum.android.diploma.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.models.VacanciesSearchResource
import ru.practicum.android.diploma.util.debouncedAction
import java.net.HttpURLConnection

class SearchFragmentViewModel(
    private val vacancyInteractor: VacancyInteractor
) : ViewModel() {
    private var lastSearchedValue: String = ""
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

    private fun processNewSearch(text: String) {
        val page = 1
        if (lastSearchedValue == text) {
            return
        }
        viewModelScope.launch {
            screenState.postValue(SearchFragmentState.RequestInProgress)
            lastSearchedValue = text
            vacancyInteractor.searchVacancies(text, page).collect { result ->
                when (result) {
                    is VacanciesSearchResource.Success -> {
                        if (result.items.isNotEmpty()) {
                            screenState.postValue(SearchFragmentState.ShowingResults(result.items, result.total))
                        } else {
                            screenState.postValue(SearchFragmentState.EmptyResults)
                        }
                    }

                    is VacanciesSearchResource.Error -> {
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
