package ru.practicum.android.diploma.data.dto.response

import ru.practicum.android.diploma.data.dto.RegionDto

data class RegionsResponse(
    val items: List<RegionDto>
) : Response()
