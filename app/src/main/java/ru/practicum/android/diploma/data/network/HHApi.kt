package ru.practicum.android.diploma.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.dto.VacanciesSearchResponse

interface HHApi {
    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: ${BuildConfig.USER_AGENT}"
    )
    @GET("/vacancies")
    suspend fun getVacancies(@Query("text") text: String, @Query("page") page: Int): Response<VacanciesSearchResponse>
}
