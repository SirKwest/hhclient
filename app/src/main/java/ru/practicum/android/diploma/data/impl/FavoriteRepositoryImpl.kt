package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.db.converters.VacancyDbConverter
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.repository.FavoriteRepository

class FavoriteRepositoryImpl(
    private val converter: VacancyDbConverter,
    private val appDatabase: AppDatabase
) : FavoriteRepository {

    override suspend fun insertVacancyToFavorite(vacancy: Vacancy) {
        val vacancyEntityDB = converter.mapToVacancyEntity(vacancy)
        appDatabase.vacancyDao().insertVacancy(vacancyEntityDB)
    }

    override suspend fun removeVacancyFromFavorite(vacancy: Vacancy) {
        val vacancyEntityDB = converter.mapToVacancyEntity(vacancy)
        appDatabase.vacancyDao().removeVacancy(vacancyEntityDB)
    }

    override suspend fun getFavoriteVacancies(): Flow<List<Vacancy>> {
        return appDatabase.vacancyDao().getVacancies().map { listVacancyEntities ->
            listVacancyEntities.map { vacancyEntityDB ->
                converter.mapToVacancy(vacancyEntityDB)
            }
        }
    }

    override suspend fun getFavoriteVacanciesId(): Flow<List<String>> {
        return appDatabase.vacancyDao().getVacancyIds()
    }

}
