package ru.practicum.android.diploma.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Resource

interface VacancyRepository {
    suspend fun searchVacancies(page: Int, options: Map<String, String>): Flow<Resource>
}
