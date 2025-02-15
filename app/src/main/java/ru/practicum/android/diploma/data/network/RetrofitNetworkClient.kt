package ru.practicum.android.diploma.data.network

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import retrofit2.HttpException
import ru.practicum.android.diploma.data.dto.request.CountriesRequest
import ru.practicum.android.diploma.data.dto.request.IndustriesRequest
import ru.practicum.android.diploma.data.dto.request.RegionsRequest
import ru.practicum.android.diploma.data.dto.request.VacanciesSearchRequest
import ru.practicum.android.diploma.data.dto.request.VacancyByIdRequest
import ru.practicum.android.diploma.data.dto.response.CountriesResponse
import ru.practicum.android.diploma.data.dto.response.IndustriesResponse
import ru.practicum.android.diploma.data.dto.response.RegionsResponse
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
            is IndustriesRequest -> getIndustriesResponse(dto)
            is CountriesRequest -> getCountries(dto)
            is RegionsRequest -> getRegions(dto)
            else -> Response()
        }
    }

    private suspend fun getVacanciesSearchResponse(
        request: VacanciesSearchRequest
    ): Response {
        return try {
            val result = api.getVacancies(request.options)
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

    private suspend fun getIndustriesResponse(
        request: IndustriesRequest
    ): Response {
        return try {
            val result = api.getIndustries()
            val body = result.body()
            val response = if (body != null) IndustriesResponse(body) else Response()
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

    private suspend fun getRegions(
        request: RegionsRequest
    ): Response {
        return try {
            if (request.countryId == null) {
                val result = api.getRegions()
                val body = result.body()
                val response = if (body != null) RegionsResponse(body) else Response()
                response.responseCode = result.code()
                response
            } else {
                val result = api.getRegionsOfCountry(request.countryId)
                val body = result.body()
                val response = if (body != null) RegionsResponse(body.regions) else Response()
                response.responseCode = result.code()
                response
            }
        } catch (error: HttpException) {
            val response = Response()
            response.responseCode = error.code()
            response
        }
    }
}
