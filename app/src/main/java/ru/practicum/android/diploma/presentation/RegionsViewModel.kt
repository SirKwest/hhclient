package ru.practicum.android.diploma.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.domain.api.LocationInteractor
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.domain.models.RegionsResource
import ru.practicum.android.diploma.util.debouncedAction
import java.net.HttpURLConnection

class RegionsViewModel(
    private val countryId: String,
    private val locationInteractor: LocationInteractor
) : ViewModel() {

    private val screenState = MutableLiveData<RegionsFragmentState>()
    fun observeScreenState(): LiveData<RegionsFragmentState> = screenState
    private var baseRegions: List<Region>? = null
    private val filterDebounce = debouncedAction<String>(FILTER_DEBOUNCE, viewModelScope) { query ->
        filter(query)
    }

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            screenState.postValue(RegionsFragmentState.RequestInProgress)
            val flow = if (countryId.isNotBlank()) {
                locationInteractor.getRegions(countryId)
            } else {
                locationInteractor.getRegions(null)
            }
            flow.collect { resource ->
                when (resource) {
                    is RegionsResource.Success -> {
                        processSuccessResult(resource)
                    }

                    is RegionsResource.Error -> {
                        processErrorResult(resource)
                    }
                }
            }
        }
    }

    fun onQueryChanged(newQuery: String) {
        filterDebounce(newQuery)
    }

    private
    fun filter(query: String) {
        viewModelScope.launch {
            baseRegions?.let { regions ->
                val filteredList = withContext(Dispatchers.Default) {
                    regions.filter { region ->
                        region.name.lowercase().contains(query.lowercase())
                    }
                }

                screenState.value = if (filteredList.isEmpty()) {
                    RegionsFragmentState.EmptyResults
                } else {
                    RegionsFragmentState.ShowingResults(filteredList)
                }
            }
        }
    }

    private fun processSuccessResult(resource: RegionsResource.Success) {
        val flattedItems = flatRegions(resource.items)
        baseRegions = flattedItems
        if (flattedItems.isNotEmpty()) {
            screenState.postValue(RegionsFragmentState.ShowingResults(flattedItems))
        } else {
            screenState.postValue(RegionsFragmentState.EmptyResults)
        }
    }

    private fun processErrorResult(resource: RegionsResource.Error) {
        when (resource.code) {
            HttpURLConnection.HTTP_BAD_REQUEST,
            HttpURLConnection.HTTP_FORBIDDEN,
            HttpURLConnection.HTTP_NOT_FOUND -> {
                screenState.postValue(RegionsFragmentState.ServerError)
            }

            else -> {
                screenState.postValue(RegionsFragmentState.NoInternetAccess)
            }
        }
    }

    private fun flatRegions(regions: List<Region>): List<Region> {
        return regions.flatMap {
            flatRegions(it.regions).toMutableList().apply {
                if (it.parentId != null) {
                    add(0, it)
                }
            }
        }
    }

    companion object {
        private const val FILTER_DEBOUNCE = 200L
    }
}
