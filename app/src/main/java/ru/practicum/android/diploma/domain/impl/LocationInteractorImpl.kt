package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.LocationInteractor
import ru.practicum.android.diploma.domain.models.CountriesResource
import ru.practicum.android.diploma.domain.repository.LocationRepository

class LocationInteractorImpl(private val locationRepository: LocationRepository) : LocationInteractor {
    override fun getCountries(): Flow<CountriesResource> {
        return locationRepository.getCountries()
    }
}
