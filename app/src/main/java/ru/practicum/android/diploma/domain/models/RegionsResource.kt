package ru.practicum.android.diploma.domain.models

sealed class RegionsResource {
    data class Success(val items: List<Region>) : RegionsResource()
    data class Error(val code: Int) : RegionsResource()
}
