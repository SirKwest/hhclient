package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.models.SearchOptions
import ru.practicum.android.diploma.domain.models.VacanciesSearchResource
import ru.practicum.android.diploma.domain.models.VacancyByIdResource
import ru.practicum.android.diploma.domain.repository.VacancyRepository

class VacancyInteractorImpl(private val repository: VacancyRepository) : VacancyInteractor {
    override fun searchVacancies(searchOptions: SearchOptions): Flow<VacanciesSearchResource> {
        return repository.searchVacancies(searchOptions)
    }

    override fun getVacancyById(id: String): Flow<VacancyByIdResource> {
        return repository.getVacancyById(id)
    }
}
