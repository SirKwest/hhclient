package ru.practicum.android.diploma.domain.models

data class Filter(
    var workPlace: Area? = null,
    var industry: Industry? = null,
    var salary: Int? = null,
    var isExistSalary: Boolean? = null
)
