package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.repository.VacancyRepository

class VacancyInteractorImpl(private val repository: VacancyRepository) : VacancyInteractor {

    override suspend fun searchVacancies(page: Int, options: Map<String, String>): Flow<Resource> {
        return repository.searchVacancies(page, options)
    }
}
