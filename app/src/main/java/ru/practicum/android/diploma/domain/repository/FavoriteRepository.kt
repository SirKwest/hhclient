package ru.practicum.android.diploma.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyFromDatabaseResource
import ru.practicum.android.diploma.domain.models.VacancyShortFromDatabaseResource

interface FavoriteRepository {
    suspend fun insertVacancyToFavorite(vacancy: Vacancy)
    suspend fun removeVacancyFromFavorite(id: String)
    suspend fun getFavoriteVacancies(): Flow<VacancyShortFromDatabaseResource>
    suspend fun getFavoriteVacancyById(id: String): Flow<VacancyFromDatabaseResource>
}
