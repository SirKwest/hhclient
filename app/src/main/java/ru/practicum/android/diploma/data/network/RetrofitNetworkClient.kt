package ru.practicum.android.diploma.data.network

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import retrofit2.HttpException
import ru.practicum.android.diploma.data.dto.request.CountriesRequest
import ru.practicum.android.diploma.data.dto.request.VacanciesSearchRequest
import ru.practicum.android.diploma.data.dto.request.VacancyByIdRequest
import ru.practicum.android.diploma.data.dto.response.CountriesResponse
import ru.practicum.android.diploma.data.dto.response.Response
import java.net.HttpURLConnection

class RetrofitNetworkClient(
    private val api: HHApi,
    private val connectivityManager: ConnectivityManager
) : NetworkClient {
    private var isConnected: Boolean = false

    init {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            // network is available for use
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                isConnected = true
            }

            // Network capabilities have changed for the network
            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                isConnected = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            }

            // lost network connection
            override fun onLost(network: Network) {
                super.onLost(network)
                isConnected = false
            }
        }

        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected) {
            return Response().apply { responseCode = HttpURLConnection.HTTP_UNAVAILABLE }
        }
        return when (dto) {
            is VacanciesSearchRequest -> getVacanciesSearchResponse(dto)
            is VacancyByIdRequest -> getVacancyByIdResponse(dto)
            is CountriesRequest -> getCountries(dto)
            else -> Response()
        }
    }

    private suspend fun getVacanciesSearchResponse(
        request: VacanciesSearchRequest
    ): Response {
        return try {
            val result = api.getVacancies(request.text, request.page)
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

    private suspend fun getCountries(
        request: CountriesRequest
    ): Response {
        return try {
            val result = api.getCountries()
            val body = result.body()
            val response = if (body != null) CountriesResponse(body) else Response()
            response.responseCode = result.code()
            response
        } catch (error: HttpException) {
            val response = Response()
            response.responseCode = error.code()
            response
        }
    }
}
