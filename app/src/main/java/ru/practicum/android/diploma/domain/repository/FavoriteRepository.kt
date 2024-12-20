package ru.practicum.android.diploma.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyFromDatabaseResource

interface FavoriteRepository {
    suspend fun insertVacancyToFavorite(vacancy: Vacancy)
    suspend fun removeVacancyFromFavorite(vacancy: Vacancy)
    suspend fun getFavoriteVacancies(): Flow<VacancyFromDatabaseResource>
    suspend fun getFavoriteVacanciesId(): Flow<List<String>>
}
