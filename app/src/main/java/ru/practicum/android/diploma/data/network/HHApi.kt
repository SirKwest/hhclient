package ru.practicum.android.diploma.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.dto.AreasResponse
import ru.practicum.android.diploma.data.dto.Industries
import ru.practicum.android.diploma.data.dto.IndustriesResponse
import ru.practicum.android.diploma.data.dto.RegionsResponse
import ru.practicum.android.diploma.data.dto.VacanciesSearchResponse

interface HHApi {
    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: ${BuildConfig.USER_AGENT}"
    )
    @GET("/vacancies")
    suspend fun getVacancies(@QueryMap options: Map<String, String>): Response<VacanciesSearchResponse>

    @GET("/areas/countries")
    suspend fun getAreas(): Response<AreasResponse>

    @GET("/areas")
    suspend fun getRegionsOfArea(@Query("areaId") areaId: String): Response<RegionsResponse>

    @GET("/industries")
    suspend fun getIndustries(): Response<IndustriesResponse>





}
