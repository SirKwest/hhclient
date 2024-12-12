package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.Response

class RetrofitNetworkClient(val api: HHApi) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        return Response()
    }
}
