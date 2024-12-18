package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy

interface FavoriteInteractor {
    suspend fun insertVacancyToFavorite(vacancy: Vacancy)
    suspend fun removeVacancyFromFavorite(vacancy: Vacancy)
    suspend fun getFavoriteVacancies(): Flow<List<Vacancy>>
}
