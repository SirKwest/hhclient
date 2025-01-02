package ru.practicum.android.diploma.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.LocationInteractor
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.domain.models.RegionsResource
import java.net.HttpURLConnection

class RegionsViewModel(
    private val countryId: String,
    private val locationInteractor: LocationInteractor
) : ViewModel() {

    private val screenState = MutableLiveData<RegionsFragmentState>()
    fun observeScreenState(): LiveData<RegionsFragmentState> = screenState
    private var baseRegions: List<Region>? = null
    private var parentRegions: List<Region>? = null

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
                        val flattedItems = flatRegions(resource.items)
                        parentRegions = flattedItems.filter { it.regions.isNotEmpty() }
                        val regionsWithoutCountry = flattedItems.filter { it.parentId != null }
                        baseRegions = regionsWithoutCountry
                        if (regionsWithoutCountry.isNotEmpty()) {
                            screenState.postValue(RegionsFragmentState.ShowingResults(regionsWithoutCountry))
                        } else {
                            screenState.postValue(RegionsFragmentState.EmptyResults)
                        }
                    }

                    is RegionsResource.Error -> {
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
                }
            }
        }
    }

    fun filter(query: String) {
        baseRegions?.let { regions ->
            val filteredList = regions.filter { region -> region.name.lowercase().contains(query.lowercase()) }
            if (filteredList.isEmpty()) {
                screenState.value = RegionsFragmentState.EmptyResults
            } else {
                screenState.value = RegionsFragmentState.ShowingResults(filteredList)
            }
        }
    }

    private fun flatRegions(regions: List<Region>): List<Region> {
        return regions.flatMap {
            flatRegions(it.regions).toMutableList().apply {
                add(0, it)
            }
        }
    }

    fun hasCountry(): Boolean = countryId.isNotBlank()

    fun getCountryForRegion(region: Region): Country {
        return if (region.parentId == null) {
            Country(region.id, region.name)
        } else {
            getCountryForRegion(parentRegions!!.first { it.id == region.parentId })
        }
    }
}
