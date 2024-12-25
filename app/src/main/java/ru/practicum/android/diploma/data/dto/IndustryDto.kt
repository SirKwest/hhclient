package ru.practicum.android.diploma.data.dto

data class IndustryGroupDto(
    val id: String,
    val industries: List<IndustryDto>,
    val name: String
)

data class IndustryDto(
    val id: String,
    val name: String
)
