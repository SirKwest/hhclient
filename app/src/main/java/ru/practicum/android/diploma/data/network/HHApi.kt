package ru.practicum.android.diploma.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.dto.CountryDto
import ru.practicum.android.diploma.data.dto.IndustryGroupDto
import ru.practicum.android.diploma.data.dto.RegionDto
import ru.practicum.android.diploma.data.dto.response.VacanciesSearchResponse
import ru.practicum.android.diploma.data.dto.response.VacancyByIdResponse

interface HHApi {
    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: ${BuildConfig.USER_AGENT}"
    )
    @GET("/vacancies")
    suspend fun getVacancies(@QueryMap options: Map<String, String>): Response<VacanciesSearchResponse>

    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: ${BuildConfig.USER_AGENT}"
    )
    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancyById(@Path("vacancy_id") id: String): Response<VacancyByIdResponse>

    @GET("/industries")
    suspend fun getIndustries(): Response<List<IndustryGroupDto>>

    @GET("/areas")
    suspend fun getCountries(): Response<List<CountryDto>>

    @GET("/areas")
    suspend fun getRegions(): Response<List<RegionDto>>

    @GET("/areas/{area_id}")
    suspend fun getRegionsOfCountry(@Path("area_id") countryId: String): Response<RegionDto>
}
