package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.CountriesResource
import ru.practicum.android.diploma.domain.models.RegionsResource

interface LocationInteractor {
    fun getCountries(): Flow<CountriesResource>
    fun getRegions(countryId: String): Flow<RegionsResource>
}
