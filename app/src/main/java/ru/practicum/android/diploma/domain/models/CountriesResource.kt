package ru.practicum.android.diploma.domain.models

sealed class CountriesResource {
    data class Success(val items: List<Country>) : CountriesResource()
    data class Error(val code: Int) : CountriesResource()
}
