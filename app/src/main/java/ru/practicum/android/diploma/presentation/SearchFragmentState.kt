package ru.practicum.android.diploma.presentation

import ru.practicum.android.diploma.domain.models.VacancyShort

sealed interface SearchFragmentState {
    data object NoInternetAccess : SearchFragmentState
    data object RequestInProgress : SearchFragmentState
    data object ServerError : SearchFragmentState
    data object EmptyResults : SearchFragmentState
    data class ShowingResults(val vacancies: List<VacancyShort>, val total: Int = 0) : SearchFragmentState
    data object LoadingNewPageOfResults : SearchFragmentState
    data object Default : SearchFragmentState
}
