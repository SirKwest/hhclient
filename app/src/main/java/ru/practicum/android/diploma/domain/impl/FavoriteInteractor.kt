package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.FavoriteInteractor
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.repository.FavoriteRepository

class FavoriteInteractor(private val favoriteRepository: FavoriteRepository) : FavoriteInteractor {
    override suspend fun insertVacancyToFavorite(vacancy: Vacancy) {
        favoriteRepository.insertVacancyToFavorite(vacancy)
    }

    override suspend fun removeVacancyFromFavorite(vacancy: Vacancy) {
        favoriteRepository.removeVacancyFromFavorite(vacancy)
    }

    override suspend fun getFavoriteVacancies(): Flow<List<Vacancy>> {
        return favoriteRepository.getFavoriteVacancies()
    }
}
