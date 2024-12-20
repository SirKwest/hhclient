package ru.practicum.android.diploma.domain.models

sealed class VacancyFromDatabaseResource {
    data class Success(val records: List<VacancyShort>): VacancyFromDatabaseResource()
    data class Error(val errorCode: Int): VacancyFromDatabaseResource()
}
