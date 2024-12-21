package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.dto.request.VacanciesSearchRequest
import ru.practicum.android.diploma.data.dto.request.VacancyByIdRequest
import ru.practicum.android.diploma.data.dto.response.VacanciesSearchResponse
import ru.practicum.android.diploma.data.dto.response.VacancyByIdResponse
import ru.practicum.android.diploma.data.dto.toVacancy
import ru.practicum.android.diploma.data.dto.toVacancyShort
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.models.VacanciesSearchResource
import ru.practicum.android.diploma.domain.models.VacancyByIdResource
import ru.practicum.android.diploma.domain.repository.VacancyRepository
import java.net.HttpURLConnection

class VacancyRepositoryImpl(
    private val headhunterClient: NetworkClient,
    private val appDatabase: AppDatabase
) : VacancyRepository {
    override fun searchVacancies(text: String, page: Int): Flow<VacanciesSearchResource> = flow {
        val response = headhunterClient.doRequest(VacanciesSearchRequest(text, page))
        if (
            response.responseCode != HttpURLConnection.HTTP_OK
            || response !is VacanciesSearchResponse
        ) {
            emit(VacanciesSearchResource.Error(response.responseCode))
        } else {
            val data = response.items.map { it.toVacancyShort() }
            emit(VacanciesSearchResource.Success(data, response.page, response.pages, response.found))
        }
    }

    override fun getVacancyById(id: String): Flow<VacancyByIdResource> = flow {
        val response = headhunterClient.doRequest(VacancyByIdRequest(id))
        if (response.responseCode == HttpURLConnection.HTTP_OK && response is VacancyByIdResponse) {
            emit(VacancyByIdResource.Success(response.toVacancy()))
            val vacancy = response.toVacancy().apply {
                isFavorite = appDatabase.vacancyDao().isVacancyRecordExists(response.id)
            }
            emit(VacancyByIdResource.Success(vacancy))
        } else {
            emit(VacancyByIdResource.Error(response.responseCode))
        }
    }
}
