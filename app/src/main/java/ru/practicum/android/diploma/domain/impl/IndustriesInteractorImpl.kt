package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.IndustriesInteractor
import ru.practicum.android.diploma.domain.models.IndustryResource
import ru.practicum.android.diploma.domain.repository.IndustriesRepository

class IndustriesInteractorImpl(private val industriesRepository: IndustriesRepository) : IndustriesInteractor {
    override fun getIndustries(): Flow<IndustryResource> {
        return industriesRepository.getIndustries()
    }

}
