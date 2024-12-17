package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Resource

interface VacancyInteractor {
    fun searchVacancies(text: String, page: Int): Flow<Resource>
    fun searchVacanciesByOptions(page: Int, options: Map<String, String>): Flow<Resource>
}
