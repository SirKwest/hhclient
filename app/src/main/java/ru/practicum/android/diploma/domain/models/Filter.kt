package ru.practicum.android.diploma.domain.models

data class Filter(
    val workPlace: String? = null,
    val industry: String? = null,
    val salary: Int? = null,
    val isExistSalary: Boolean = false
)
