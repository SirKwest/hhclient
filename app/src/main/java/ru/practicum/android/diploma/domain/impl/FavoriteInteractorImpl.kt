package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.FavoriteInteractor
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyFromDatabaseResource
import ru.practicum.android.diploma.domain.models.VacancyShortFromDatabaseResource
import ru.practicum.android.diploma.domain.repository.FavoriteRepository

class FavoriteInteractorImpl(private val favoriteRepository: FavoriteRepository) : FavoriteInteractor {
    override suspend fun insertVacancyToFavorite(vacancy: Vacancy) {
        favoriteRepository.insertVacancyToFavorite(vacancy)
    }

    override suspend fun removeVacancyFromFavorite(id: String) {
        favoriteRepository.removeVacancyFromFavorite(id)
    }

    override suspend fun getFavoriteVacanciesShortList(): Flow<VacancyShortFromDatabaseResource> {
        return favoriteRepository.getFavoriteVacancies()
    }

    override suspend fun getFavoriteVacancyById(id: String): Flow<VacancyFromDatabaseResource> {
        return favoriteRepository.getFavoriteVacancyById(id)
    }
}
