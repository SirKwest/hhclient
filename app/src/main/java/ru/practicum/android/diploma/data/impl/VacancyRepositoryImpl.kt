package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.request.VacanciesSearchRequest
import ru.practicum.android.diploma.data.dto.request.VacancyByIdRequest
import ru.practicum.android.diploma.data.dto.response.VacanciesSearchResponse
import ru.practicum.android.diploma.data.dto.response.VacancyByIdResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.models.VacanciesSearchResource
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyByIdResource
import ru.practicum.android.diploma.domain.models.VacancyShort
import ru.practicum.android.diploma.domain.repository.VacancyRepository
import java.net.HttpURLConnection

class VacancyRepositoryImpl(private val headhunterClient: NetworkClient) : VacancyRepository {
    override fun searchVacancies(text: String, page: Int): Flow<VacanciesSearchResource> = flow {
        val response = headhunterClient.doRequest(VacanciesSearchRequest(text, page))
        if (
            response.responseCode != HttpURLConnection.HTTP_OK
            || response !is VacanciesSearchResponse
        ) {
            emit(VacanciesSearchResource.Error(response.responseCode))
        } else {
            val data = response.items.map {
                val logo = it.employer.logo?.original ?: (it.employer.logo?.big ?: it.employer.logo?.small)
                VacancyShort(
                    it.id,
                    it.name,
                    it.area.name,
                    it.employer.name,
                    logo.orEmpty(),
                    it.salary?.low,
                    it.salary?.high,
                    it.salary?.currency,
                )
            }
            emit(VacanciesSearchResource.Success(data, response.page, response.pages, response.found))
        }
    }

    override fun getVacancyById(id: String): Flow<VacancyByIdResource> = flow {
        val response = headhunterClient.doRequest(VacancyByIdRequest(id))
        if (response.responseCode == HttpURLConnection.HTTP_OK && response is VacancyByIdResponse) {
            val logo =
                response.employer.logo?.original ?: (response.employer.logo?.big ?: response.employer.logo?.small)
            val vacancy = Vacancy(
                id = response.id,
                name = response.name,
                area = response.area.name,
                employer = response.employer.name,
                logo = logo.orEmpty(),
                salaryLow = response.salary?.low,
                salaryHigh = response.salary?.high,
                currency = response.salary?.currency,
                keySkills = response.keySkills.map { it.name },
                description = response.description,
                experience = response.experience?.name.orEmpty(),
                employment = response.employment?.name.orEmpty(),
                schedule = response.schedule?.name.orEmpty(),
                url = response.url
            )
            emit(VacancyByIdResource.Success(vacancy))
        } else {
            emit(VacancyByIdResource.Error(response.responseCode))
        }
    }
}
