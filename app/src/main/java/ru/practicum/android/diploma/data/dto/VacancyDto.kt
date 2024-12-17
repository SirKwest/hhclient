package ru.practicum.android.diploma.data.dto

data class VacancyDto(
    val id: String,
    val name: String,
    val area: AreaDto,
    val employer: EmployerDto,
    val salary: SalaryDto?,
    val keySkills: List<String>,
    val description: String,
    val experience: ExperienceDto?,
    val employment: EmploymentDto?,
    val schedule: ScheduleDto?
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
