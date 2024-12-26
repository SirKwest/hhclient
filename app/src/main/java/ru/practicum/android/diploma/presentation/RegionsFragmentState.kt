package ru.practicum.android.diploma.presentation

import ru.practicum.android.diploma.domain.models.Region

sealed interface RegionsFragmentState {
    data object NoInternetAccess : RegionsFragmentState
    data object RequestInProgress : RegionsFragmentState
    data object ServerError : RegionsFragmentState
    data object EmptyResults : RegionsFragmentState
    data class ShowingResults(val regions: List<Region>) : RegionsFragmentState
}
