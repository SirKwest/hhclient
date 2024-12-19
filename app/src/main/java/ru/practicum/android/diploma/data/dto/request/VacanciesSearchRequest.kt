package ru.practicum.android.diploma.data.dto.request

data class VacanciesSearchRequest(
    val page: Int,
    val options: Map<String, String>
)
