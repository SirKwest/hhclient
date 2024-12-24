package ru.practicum.android.diploma.presentation

import ru.practicum.android.diploma.domain.models.Country

sealed interface CountriesFragmentState {
    data object NoInternetAccess : CountriesFragmentState
    data object RequestInProgress : CountriesFragmentState
    data object ServerError : CountriesFragmentState
    data class ShowingResults(val countries: List<Country>) : CountriesFragmentState
}
