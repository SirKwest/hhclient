package ru.practicum.android.diploma.data.db.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.model.VacancyEntityDB

interface VacancyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(vacancy: VacancyEntityDB)

    @Delete(entity = VacancyEntityDB::class)
    suspend fun removeVacancy(vacancy: VacancyEntityDB)

    @Query("SELECT * from vacancy_table")
    fun getVacancies(): Flow<List<VacancyEntityDB>>
}
