package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.request.CountriesRequest
import ru.practicum.android.diploma.data.dto.response.CountriesResponse
import ru.practicum.android.diploma.data.dto.toCountry
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.models.CountriesResource
import ru.practicum.android.diploma.domain.repository.LocationRepository
import java.net.HttpURLConnection

class LocationRepositoryImpl(private val headhunterClient: NetworkClient) : LocationRepository {
    override fun getCountries(): Flow<CountriesResource> = flow {
        val response = headhunterClient.doRequest(CountriesRequest)
        if (response.responseCode == HttpURLConnection.HTTP_OK && response is CountriesResponse) {
            emit(CountriesResource.Success(response.items.map { it.toCountry() }))
        } else {
            emit(CountriesResource.Error(response.responseCode))
        }
    }
}
