package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyShort

interface VacancyInteractor {
    fun searchVacancies(text: String, page: Int): Flow<Resource>
}
