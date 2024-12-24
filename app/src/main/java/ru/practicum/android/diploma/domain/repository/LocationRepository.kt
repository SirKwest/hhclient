package ru.practicum.android.diploma.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.CountriesResource

interface LocationRepository {
    fun getCountries(): Flow<CountriesResource>
}
