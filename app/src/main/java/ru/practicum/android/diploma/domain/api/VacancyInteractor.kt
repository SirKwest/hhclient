package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Resource

interface VacancyInteractor {
    suspend fun searchVacancies(page: Int, options: Map<String, String>): Flow<Resource>
}
