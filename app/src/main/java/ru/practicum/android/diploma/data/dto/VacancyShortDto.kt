package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class VacancyShortDto(
    val id: String,
    val name: String,
    val area: AreaDto,
    val employer: EmployerDto,
    val salary: SalaryDto? = SalaryDto(),
)

data class AreaDto(
    val id: String,
    val name: String,
    val url: String
)

data class EmployerDto(
    val id: String,
    val name: String,
    @SerializedName("logo_urls") val logo: LogoDto? = LogoDto()
)

data class LogoDto(
    @SerializedName("90") val small: String? = null,
    @SerializedName("240") val big: String? = null,
    val original: String? = null
)

data class SalaryDto(
    val currency: String? = null,
    @SerializedName("from") val low: Int? = null,
    @SerializedName("to") val high: Int? = null,
)

data class ScheduleDto(
    val id: String?,
    val name: String
)

data class ExperienceDto(
    val id: String,
    val name: String
)

data class EmploymentDto(
    val id: String?,
    val name: String
)

data class KeySkill(
    val name: String
)

data class IndustryDto(
    val id: String,
    val industries: List<SubIndustriesDto>,
    val name: String
)

data class SubIndustriesDto(
    val id: String,
    val name: String
)
