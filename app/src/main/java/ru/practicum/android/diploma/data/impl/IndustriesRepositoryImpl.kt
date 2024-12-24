package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.request.IndustriesRequest
import ru.practicum.android.diploma.data.dto.response.IndustriesResponse
import ru.practicum.android.diploma.data.dto.toIndustry
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.models.IndustryResource
import ru.practicum.android.diploma.domain.repository.IndustriesRepository
import java.net.HttpURLConnection

class IndustriesRepositoryImpl(private val headhunterClient: NetworkClient) : IndustriesRepository {
    override fun getIndustries(): Flow<IndustryResource> = flow {
        val response = headhunterClient.doRequest(IndustriesRequest)
        if (response.responseCode == HttpURLConnection.HTTP_OK && response is IndustriesResponse) {
            val items = response.items.map { it.toIndustry() }
            emit(IndustryResource.Success(items))
        } else {
            emit(IndustryResource.Error(response.responseCode))
        }
    }
}
