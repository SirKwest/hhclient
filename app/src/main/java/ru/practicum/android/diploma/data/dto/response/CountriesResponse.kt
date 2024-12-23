package ru.practicum.android.diploma.data.dto.response

import ru.practicum.android.diploma.data.dto.CountryDto

data class CountriesResponse(
    val items: List<CountryDto>
) : Response()

