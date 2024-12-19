package ru.practicum.android.diploma.presentation

import ru.practicum.android.diploma.domain.models.Vacancy

sealed interface VacancyDetailsFragmentState {
    data object NoInternetAccess : VacancyDetailsFragmentState
    data object RequestInProgress : VacancyDetailsFragmentState
    data object ServerError : VacancyDetailsFragmentState
    data object EmptyResults : VacancyDetailsFragmentState
    data class ShowingResults(val vacancy: Vacancy) : VacancyDetailsFragmentState
}
