package ru.practicum.android.diploma.domain.models

sealed class VacancyShortFromDatabaseResource {
    data class Success(val records: List<VacancyShort>) : VacancyShortFromDatabaseResource()
    data class Error(val errorCode: Int) : VacancyShortFromDatabaseResource()
}
