package ru.practicum.android.diploma.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacanciesSearchResource
import ru.practicum.android.diploma.domain.models.VacancyByIdResource

interface VacancyRepository {
    fun searchVacancies(options: Map<String, String>): Flow<VacanciesSearchResource>
    fun getVacancyById(id: String): Flow<VacancyByIdResource>
}
