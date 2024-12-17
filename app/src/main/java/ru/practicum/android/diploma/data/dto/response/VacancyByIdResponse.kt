package ru.practicum.android.diploma.data.dto.response

import ru.practicum.android.diploma.data.dto.VacancyDto

data class VacancyByIdResponse(
    val item: VacancyDto
) : Response()
