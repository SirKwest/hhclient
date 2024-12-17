package ru.practicum.android.diploma.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.models.Resource
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
    private var searchDebounceJob: Job? = null

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    fun addFilter() {
        val newValue = filtersButtonState.value ?: false
        filtersButtonState.postValue(!newValue)
    }

    fun searchByOptions(page: Int, options: Map<String, String>) {
        searchDebounceJob?.cancel()
        searchDebounceJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            vacancyInteractor.searchVacanciesByOptions(page, options).collect { result ->
                processResults(result)
            }
        }
    }

    private fun processResults(result: Resource) {
        when (result) {
            is Resource.Success -> {
                if (result.items.isNotEmpty()) {
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

                    else -> {
                        screenState.postValue(SearchFragmentState.NoInternetAccess)
                    }
                }
            }
        }
    }
}
