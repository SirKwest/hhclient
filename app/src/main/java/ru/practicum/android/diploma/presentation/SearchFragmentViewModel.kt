package ru.practicum.android.diploma.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.ui.search.SearchFragmentState
import ru.practicum.android.diploma.util.debouncedAction
import java.net.HttpURLConnection

class SearchFragmentViewModel(
    private val vacancyInteractor: VacancyInteractor
) : ViewModel() {
    private val data = MutableLiveData<SearchFragmentState>()
    fun observeData(): LiveData<SearchFragmentState> = data
    private var isActiveFilter = false

    val search: (String) -> Unit =
        debouncedAction(SEARCH_DEBOUNCE_DELAY, viewModelScope) { searchText ->
            processNewSearch(searchText)
        }

    private fun processNewSearch(text: String) {
        val page = 1
        viewModelScope.launch {
            vacancyInteractor.searchVacancies(text, page).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        if (result.items.isNotEmpty()) {
                            data.postValue(SearchFragmentState.ShowingResults(result.items, result.total))
                        } else {
                            data.postValue(SearchFragmentState.EmptyResults)
                        }
                    }

                    is Resource.Error -> {
                        when (result.code) {
                            HttpURLConnection.HTTP_BAD_REQUEST -> { data.postValue(SearchFragmentState.ServerError) }
                            HttpURLConnection.HTTP_FORBIDDEN -> { data.postValue(SearchFragmentState.ServerError) }
                            HttpURLConnection.HTTP_NOT_FOUND -> { data.postValue(SearchFragmentState.ServerError) }
                            else -> { data.postValue(SearchFragmentState.NoInternetAccess) }
                        }
                    }
                }
            }
        }
    }

    fun addFilter() {
        if (isActiveFilter) {
            isActiveFilter = false
            data.postValue(SearchFragmentState.FilterState(isActiveFilter))
        } else {
            isActiveFilter = true
            data.postValue(SearchFragmentState.FilterState(isActiveFilter))
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
