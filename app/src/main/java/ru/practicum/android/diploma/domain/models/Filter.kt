package ru.practicum.android.diploma.domain.models

data class Filter(
    val workPlace: String?,
    val industry: String?,
    val salary: Int?,
    val isExistSalary: Boolean
)
