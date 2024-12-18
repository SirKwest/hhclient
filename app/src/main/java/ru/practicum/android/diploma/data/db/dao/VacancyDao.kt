package ru.practicum.android.diploma.data.db.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.practicum.android.diploma.data.db.model.VacancyEntityDB

interface VacancyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(vacancy: VacancyEntityDB)

    @Delete(entity = VacancyEntityDB::class)
    suspend fun removeVacancy(vacancy: VacancyEntityDB)
}
