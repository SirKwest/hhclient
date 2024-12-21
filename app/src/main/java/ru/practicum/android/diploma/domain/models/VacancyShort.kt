package ru.practicum.android.diploma.domain.models

data class VacancyShort(
    val id: String,
    val name: String,
    val location: String,
    val employer: String,
    val logo: String?,
    val salaryLow: Int?,
    val salaryHigh: Int?,
    val currency: String?,
)
