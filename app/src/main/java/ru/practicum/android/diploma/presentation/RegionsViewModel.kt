package ru.practicum.android.diploma.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.LocationInteractor
import ru.practicum.android.diploma.domain.models.RegionsResource
import java.net.HttpURLConnection

class RegionsViewModel(
    private val countryId: String,
    private val locationInteractor: LocationInteractor
) : ViewModel() {

    private val screenState = MutableLiveData<RegionsFragmentState>()
    fun observeScreenState(): LiveData<RegionsFragmentState> = screenState

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
                        screenState.postValue(RegionsFragmentState.ShowingResults(resource.items))
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
}