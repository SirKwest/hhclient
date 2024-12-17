package ru.practicum.android.diploma.domain.models

import kotlinx.coroutines.flow.Flow

interface VacancyRepository {
    fun searchVacancies(text: String, page: Int): Flow<Resource>
    fun searchVacanciesByOptions(page: Int, options: Map<String, String>): Flow<Resource>
}
