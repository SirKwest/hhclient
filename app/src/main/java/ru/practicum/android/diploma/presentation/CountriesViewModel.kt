package ru.practicum.android.diploma.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.LocationInteractor
import ru.practicum.android.diploma.domain.models.CountriesResource
import java.net.HttpURLConnection

class CountriesViewModel(
    private val locationInteractor: LocationInteractor
) : ViewModel() {

    private val screenState = MutableLiveData<CountriesFragmentState>()
    fun observeScreenState(): LiveData<CountriesFragmentState> = screenState

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            screenState.postValue(CountriesFragmentState.RequestInProgress)
            locationInteractor.getCountries().collect { resource ->
                when (resource) {
                    is CountriesResource.Success -> {
                        screenState.postValue(CountriesFragmentState.ShowingResults(resource.items))
                    }

                    is CountriesResource.Error -> {
                        when (resource.code) {
                            HttpURLConnection.HTTP_BAD_REQUEST,
                            HttpURLConnection.HTTP_FORBIDDEN,
                            HttpURLConnection.HTTP_NOT_FOUND -> {
                                screenState.postValue(CountriesFragmentState.ServerError)
                            }

                            else -> {
                                screenState.postValue(CountriesFragmentState.NoInternetAccess)
                            }
                        }
                    }
                }
            }
        }
    }
}
