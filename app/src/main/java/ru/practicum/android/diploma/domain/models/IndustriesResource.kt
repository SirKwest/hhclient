package ru.practicum.android.diploma.domain.models

sealed class IndustriesResource {
    data class Success(val industries: List<IndustryGroup>) : IndustriesResource()
    data class Error(val code: Int) : IndustriesResource()
}
