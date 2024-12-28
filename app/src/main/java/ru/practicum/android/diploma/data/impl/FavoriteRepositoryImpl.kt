package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.db.converters.VacancyDbConverter
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyFromDatabaseResource
import ru.practicum.android.diploma.domain.models.VacancyShortFromDatabaseResource
import ru.practicum.android.diploma.domain.repository.FavoriteRepository
import java.sql.SQLException

class FavoriteRepositoryImpl(
    private val converter: VacancyDbConverter,
    private val appDatabase: AppDatabase
) : FavoriteRepository {

    override suspend fun insertVacancyToFavorite(vacancy: Vacancy) {
        val vacancyEntityDB = converter.mapToVacancyEntity(vacancy)
        appDatabase.vacancyDao().insertVacancy(vacancyEntityDB)
    }

    override suspend fun removeVacancyFromFavorite(id: String) {
        appDatabase.vacancyDao().removeVacancy(id)
    }

    override suspend fun getFavoriteVacancies(): Flow<VacancyShortFromDatabaseResource> = flow {
        try {
            appDatabase.vacancyDao().getVacancies().collect { records ->
                emit(VacancyShortFromDatabaseResource.Success(
                    records.map { record -> converter.mapToVacancyShort(record) }
                ))
            }
        } catch (error: SQLException) {
            emit(VacancyShortFromDatabaseResource.Error(error.errorCode))
        }
    }

    override suspend fun getFavoriteVacancyById(id: String): Flow<VacancyFromDatabaseResource> = flow {
        try {
            val entity = appDatabase.vacancyDao().getVacancyById(id)
            if (entity == null) {
                emit(VacancyFromDatabaseResource.Error(0))
            } else {
                emit(
                    VacancyFromDatabaseResource.Success(
                        converter.mapToVacancy(entity)
                    )
                )
            }
        } catch (error: SQLException) {
            emit(VacancyFromDatabaseResource.Error(error.errorCode))
        }
    }
}
