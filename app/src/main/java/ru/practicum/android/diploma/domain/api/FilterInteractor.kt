package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Industry

interface FilterInteractor {
    fun saveFilter(filter: Filter)
    fun saveArea(area: Area)
    fun saveIndustry(industry: Industry)
    fun getFilter(): Filter
    fun getArea(): Area
    fun getIndustry(): Industry
    fun updateFilter(filter: Filter)
    fun isFiltersSaved(): Boolean
}
