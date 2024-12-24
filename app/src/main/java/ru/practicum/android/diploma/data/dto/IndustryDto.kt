package ru.practicum.android.diploma.data.dto

data class IndustryDto(
    val id: String,
    val industries: List<SubIndustryDto>,
    val name: String
)

data class SubIndustryDto(
    val id: String,
    val name: String
)
