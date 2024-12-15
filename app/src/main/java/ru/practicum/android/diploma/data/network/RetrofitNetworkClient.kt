package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.VacanciesSearchRequest
import ru.practicum.android.diploma.data.dto.VacanciesSearchResponse
import java.net.HttpURLConnection

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
    ): Response {
        return try {
            val result = api.getVacancies(request.text)
            val response = result.body() ?: Response()
            response.responseCode = result.code()
            response
        } catch (error: Exception) {
            val response = Response()
            response.responseCode = HttpURLConnection.HTTP_UNAVAILABLE
            response
        }
    }
}
