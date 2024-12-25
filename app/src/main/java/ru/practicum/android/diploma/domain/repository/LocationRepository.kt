package ru.practicum.android.diploma.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.CountriesResource
import ru.practicum.android.diploma.domain.models.RegionsResource

interface LocationRepository {
    fun getCountries(): Flow<CountriesResource>
    fun getRegions(countryId: String?): Flow<RegionsResource>
}
