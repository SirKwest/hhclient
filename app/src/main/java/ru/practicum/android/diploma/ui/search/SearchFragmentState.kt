package ru.practicum.android.diploma.ui.search

import ru.practicum.android.diploma.domain.models.VacancyShort

sealed interface SearchFragmentState {
    data object NoInternetAccess : SearchFragmentState
    data object RequestInProgress : SearchFragmentState
    data object ServerError : SearchFragmentState
    data object EmptyResults : SearchFragmentState
    data class ShowingResults(val vacancies: List<VacancyShort>, val total: Int) : SearchFragmentState
    data object LoadingNewPageOfResults : SearchFragmentState
    data object Default : SearchFragmentState
    data class FilterState(val isActive: Boolean) : SearchFragmentState
}
