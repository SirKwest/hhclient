package ru.practicum.android.diploma.domain.models

data class Filter(
    val workPlace: Area? = null,
    val industry: Industry? = null,
    val salary: Long? = null,
    val isExistSalary: Boolean? = null
)
