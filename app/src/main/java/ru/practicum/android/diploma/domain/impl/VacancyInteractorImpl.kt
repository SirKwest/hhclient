package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.models.VacanciesSearchResource
import ru.practicum.android.diploma.domain.models.VacancyByIdResource
import ru.practicum.android.diploma.domain.repository.VacancyRepository

class VacancyInteractorImpl(private val repository: VacancyRepository) : VacancyInteractor {
    override fun searchVacancies(options: Map<String, String>): Flow<VacanciesSearchResource> {
        return repository.searchVacancies(options)
    }

    override fun getVacancyById(id: String): Flow<VacancyByIdResource> {
        return repository.getVacancyById(id)
    }
}
