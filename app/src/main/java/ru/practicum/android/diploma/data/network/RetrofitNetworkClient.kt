package ru.practicum.android.diploma.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.data.dto.VacanciesSearchResponse

class RetrofitNetworkClient(val api: HHApi) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        return when (dto) {
            is VacanciesSearchRequest -> {
                getVacanciesSearchResponse(dto)
            }

            else -> {
                Response()
            }
        }
    }

    private suspend fun getVacanciesSearchResponse(
        request: VacanciesSearchRequest
    ): VacanciesSearchResponse {
        val result = api.getVacancies(request.text)
        val response = result.body() ?: VacanciesSearchResponse(listOf(), 0, 0, 0)
        response.responseCode = result.code()
        return response
    }
}
