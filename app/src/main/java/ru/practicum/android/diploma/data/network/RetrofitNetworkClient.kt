package ru.practicum.android.diploma.data.network

import retrofit2.HttpException
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.VacanciesSearchRequest

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
        } catch (error: HttpException) {
            val response = Response()
            response.responseCode = error.code()
            response
        }
    }
}
