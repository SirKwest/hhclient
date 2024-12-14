package ru.practicum.android.diploma.data.dto

class VacanciesSearchResponse(
    val items: List<VacancyShortDto>,
    val page: Int,
    val pages: Int,
    val found: Int,
) : Response()
