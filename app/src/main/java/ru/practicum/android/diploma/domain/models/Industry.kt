package ru.practicum.android.diploma.domain.models

data class Industry(
    val id: String,
    val subIndustries: List<SubIndustries>,
    val name: String
)
