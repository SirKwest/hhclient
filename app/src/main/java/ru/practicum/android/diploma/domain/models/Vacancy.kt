package ru.practicum.android.diploma.domain.models

data class Vacancy(
    val id: String,
    val name: String,
    val area: String,
    val employer: String,
    val logo: String?,
    val salaryLow: Int?,
    val salaryHigh: Int?,
    val currency: String?,
    val keySkills: List<String>,
    val description: String,
    val experience: String,
    val employment: String,
    val schedule: String,
    val url: String
)
