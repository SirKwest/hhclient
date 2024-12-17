package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.VacanciesSearchOptionsRequest
import ru.practicum.android.diploma.data.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.data.dto.VacanciesSearchResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyShort
import ru.practicum.android.diploma.domain.repository.VacancyRepository

class VacancyRepositoryImpl(private val headhunterClient: NetworkClient) : VacancyRepository {
    override suspend fun searchVacancies(text: String, page: Int): Flow<Resource> = flow {
        val response = proceedRequest(headhunterClient.doRequest(VacanciesSearchRequest(text, page)))
        emit(response)
    }

    override suspend fun searchVacanciesByOptions(page: Int, options: Map<String, String>): Flow<Resource> = flow {
        val response = proceedRequest(headhunterClient.doRequest(VacanciesSearchOptionsRequest(page, options)))
        emit(response)
    }

    private fun proceedRequest(response: Response): Resource {
        if (response is VacanciesSearchResponse) {
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
            return Resource.Success(data, response.page, response.pages, response.found)
        } else {
            return Resource.Error(response.responseCode)
        }
    }

}
