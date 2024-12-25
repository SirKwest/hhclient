package ru.practicum.android.diploma.domain.repository

import ru.practicum.android.diploma.domain.models.Filter

interface FilterRepository {
    fun saveFilter(filter: Filter)
    fun getFilter(): Filter
    fun isFiltersSaved(): Boolean
    fun updateFilter(filter: Filter)
}
