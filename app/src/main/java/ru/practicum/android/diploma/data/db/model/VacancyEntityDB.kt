package ru.practicum.android.diploma.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacancy_table")
data class VacancyEntityDB(
    @PrimaryKey
    val id: String
)
