package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.response.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}
