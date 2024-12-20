package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.model.VacancyEntityDB

@Dao
interface VacancyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(vacancy: VacancyEntityDB)

    @Query("DELETE FROM vacancy_table WHERE id = :id")
    suspend fun removeVacancy(id: String)

    @Query("SELECT * from vacancy_table")
    suspend fun getVacancies(): List<VacancyEntityDB>

    @Query("SELECT * FROM vacancy_table WHERE id = :id")
    suspend fun getVacancyById(id: String): VacancyEntityDB

    @Query("SELECT COUNT(*) > 0 FROM vacancy_table WHERE id = :id")
    suspend fun isVacancyRecordExists(id: String): Boolean
}
