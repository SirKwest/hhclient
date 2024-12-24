package ru.practicum.android.diploma.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.IndustryResource
import ru.practicum.android.diploma.domain.models.VacanciesSearchResource
import ru.practicum.android.diploma.domain.models.VacancyByIdResource

interface VacancyRepository {
    fun searchVacancies(text: String, page: Int): Flow<VacanciesSearchResource>
    fun getVacancyById(id: String): Flow<VacancyByIdResource>
    fun getIndustries(): Flow<IndustryResource>
}
