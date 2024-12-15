package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.data.dto.VacanciesSearchResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyRepository
import ru.practicum.android.diploma.domain.models.VacancyShort
import java.net.HttpURLConnection

class VacancyRepositoryImpl(private val headhunterClient: NetworkClient) : VacancyRepository {
    override fun searchVacancies(text: String, page: Int): Flow<Resource> = flow {
        val response = headhunterClient.doRequest(VacanciesSearchRequest(text, page))
        if (
            response.responseCode != HttpURLConnection.HTTP_OK
            || response !is VacanciesSearchResponse
        ) {
            emit(Resource.Error(response.responseCode))
            return@flow
        }
        val data = response.items.map {
            VacancyShort(
                it.id,
                it.name,
                it.area.name,
                it.employer.name,
                it.employer.logo?.small.orEmpty(),
                it.salary?.low,
                it.salary?.high,
                it.salary?.currency,
            )
        }
        emit(Resource.Success(data, response.page, response.pages, response.found))
    }
}
