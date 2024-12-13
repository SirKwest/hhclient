package ru.practicum.android.diploma.domain.models

import kotlinx.coroutines.flow.Flow

interface VacancyRepository {
    fun searchVacancies(text: String, page: Int): Flow<Resource>
}
