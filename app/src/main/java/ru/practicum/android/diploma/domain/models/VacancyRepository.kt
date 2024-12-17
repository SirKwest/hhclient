package ru.practicum.android.diploma.domain.models

import kotlinx.coroutines.flow.Flow

interface VacancyRepository {
    suspend fun searchVacancies(text: String, page: Int): Flow<Resource>
    suspend fun searchVacanciesByOptions(page: Int, options: Map<String, String>): Flow<Resource>
}
