package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.SearchOptions
import ru.practicum.android.diploma.domain.models.VacanciesSearchResource
import ru.practicum.android.diploma.domain.models.VacancyByIdResource

interface VacancyInteractor {
    fun searchVacancies(searchOptions: SearchOptions): Flow<VacanciesSearchResource>
    fun getVacancyById(id: String): Flow<VacancyByIdResource>
}
