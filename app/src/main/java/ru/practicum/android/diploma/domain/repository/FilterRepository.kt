package ru.practicum.android.diploma.domain.repository

import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Industry

interface FilterRepository {
    fun saveFilter(filter: Filter)
    fun saveArea(area: Area)
    fun saveIndustry(industry: Industry)
    fun getFilter(): Filter
    fun getArea(): Area
    fun getIndustry(): Industry
    fun isFiltersSaved(): Boolean
    fun updateFilter(filter: Filter)
    fun savedFromApplyButton(isApply: Boolean)
    fun getFromApply(): Boolean
}
