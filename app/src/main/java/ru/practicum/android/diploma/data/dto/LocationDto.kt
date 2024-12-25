package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class CountryDto(
    val id: String,
    val name: String
)

data class RegionDto(
    val id: String,
    val name: String,
    @SerializedName("parent_id") val parentId: String?,
    @SerializedName("areas") val regions: List<RegionDto>
)
