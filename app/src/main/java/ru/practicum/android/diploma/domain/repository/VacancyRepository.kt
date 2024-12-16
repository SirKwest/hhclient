package ru.practicum.android.diploma.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Resource

interface VacancyRepository {
    fun searchVacancies(text: String, page: Int): Flow<Resource>
}
