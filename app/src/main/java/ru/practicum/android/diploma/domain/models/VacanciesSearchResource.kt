package ru.practicum.android.diploma.domain.models

sealed class VacanciesSearchResource {
    data class Success(
        val items: List<VacancyShort>,
        val page: Int,
        val pages: Int,
        val total: Int
    ) : VacanciesSearchResource()

    data class Error(val code: Int) : VacanciesSearchResource()
}
