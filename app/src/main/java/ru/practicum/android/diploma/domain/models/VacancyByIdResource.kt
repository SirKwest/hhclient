package ru.practicum.android.diploma.domain.models

sealed class VacancyByIdResource {
    data class Success(val item: Vacancy) : VacancyByIdResource()
    data class Error(val code: Int) : VacancyByIdResource()
}
