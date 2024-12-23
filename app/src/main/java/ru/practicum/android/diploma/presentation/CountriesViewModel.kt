package ru.practicum.android.diploma.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.LocationInteractor
import ru.practicum.android.diploma.domain.models.CountriesResource

class CountriesViewModel(
    private val locationInteractor: LocationInteractor
) : ViewModel() {

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            locationInteractor.getCountries().collect { resource ->
                if (resource is CountriesResource.Success)
                    Log.d("my", "${resource.items}")
            }
        }
    }
}
