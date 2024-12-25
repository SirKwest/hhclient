package ru.practicum.android.diploma.domain.models

sealed class IndustryResource {
    data class Success(val industries: List<Industry>) : IndustryResource()
    data class Error(val errorCode: Int) : IndustryResource()
}
