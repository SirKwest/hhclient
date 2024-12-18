package ru.practicum.android.diploma.domain.repository

import ru.practicum.android.diploma.domain.models.Vacancy

interface FavoriteRepository {
    suspend fun insertVacancyToFavorite(vacancy: Vacancy)
    suspend fun removeVacancyFromFavorite(vacancy: Vacancy)
}
