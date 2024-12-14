package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class VacancyShortDto(
    val id: String,
    val name: String,
    val area: Area,
    val employer: Employer,
    val salary: Salary? = Salary(),
)

data class Area(
    val id: String,
    val name: String,
    val url: String
)

data class Employer(
    val id: String,
    val name: String,
    @SerializedName("logo_urls") val logo: Logo? = Logo()
)

data class Logo(
    @SerializedName("90") val small: String? = null,
    @SerializedName("240") val big: String? = null,
    val original: String? = null
)

data class Salary(
    val currency: String? = null,
    @SerializedName("from") val low: Int? = null,
    @SerializedName("to") val high: Int? = null,
)
