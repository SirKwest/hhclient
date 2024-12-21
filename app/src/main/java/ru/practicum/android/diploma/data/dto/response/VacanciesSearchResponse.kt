package ru.practicum.android.diploma.data.dto.response

import ru.practicum.android.diploma.data.dto.VacancyShortDto

class VacanciesSearchResponse(
    val items: List<VacancyShortDto>,
    val page: Int,
    val pages: Int,
    val found: Int,
) : Response()
