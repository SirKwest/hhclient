package ru.practicum.android.diploma.data.network

import retrofit2.HttpException
import ru.practicum.android.diploma.data.dto.request.VacanciesSearchRequest
import ru.practicum.android.diploma.data.dto.request.VacancyByIdRequest
import ru.practicum.android.diploma.data.dto.response.Response

class RetrofitNetworkClient(val api: HHApi) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        return when (dto) {
            is VacanciesSearchRequest -> {
                getVacanciesSearchResponse(dto)
            }

            is VacancyByIdRequest -> {
                getVacancyByIdResponse(dto)
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

    private suspend fun getVacancyByIdResponse(
        request: VacancyByIdRequest
    ): Response {
        return try {
            val result = api.getVacancyById(request.id)
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
