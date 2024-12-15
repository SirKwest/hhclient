package ru.practicum.android.diploma.data.dto

data class VacanciesSearchRequest(
    val options: HashMap<String, String>,
    val page: Int,
)
