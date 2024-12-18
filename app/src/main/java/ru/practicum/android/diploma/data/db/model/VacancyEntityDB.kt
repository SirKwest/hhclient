package ru.practicum.android.diploma.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacancy_table")
data class VacancyEntityDB(
    @PrimaryKey
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
