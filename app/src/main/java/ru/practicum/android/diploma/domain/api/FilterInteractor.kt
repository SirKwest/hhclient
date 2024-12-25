package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Filter

interface FilterInteractor {
    fun saveFilter(filter: Filter)
    fun getFilter(): Filter

    fun updateFilter(filter: Filter)
    fun isFiltersSaved(): Boolean
}
