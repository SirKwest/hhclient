package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyFromDatabaseResource
import ru.practicum.android.diploma.domain.models.VacancyShortFromDatabaseResource

interface FavoriteInteractor {
    suspend fun insertVacancyToFavorite(vacancy: Vacancy)
    suspend fun removeVacancyFromFavorite(id: String)
    suspend fun getFavoriteVacanciesShortList(): Flow<VacancyShortFromDatabaseResource>
    suspend fun getFavoriteVacancyById(id: String): Flow<VacancyFromDatabaseResource>
}
