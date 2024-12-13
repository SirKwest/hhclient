package ru.practicum.android.diploma.domain.models

sealed class Resource {
    data class Success(val items: List<VacancyShort>, val page: Int, val pages: Int, val total: Int) : Resource()
    data class Error(val code: Int) : Resource()
}
