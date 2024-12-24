package ru.practicum.android.diploma.presentation

import ru.practicum.android.diploma.domain.models.Industry

sealed interface IndustriesFragmentState {
    data object NoInternetAccess : IndustriesFragmentState
    data object RequestInProgress : IndustriesFragmentState
    data object ServerError : IndustriesFragmentState
    data object EmptyResults : IndustriesFragmentState
    data class ShowingResults(val industries: List<Industry>) : IndustriesFragmentState
}
