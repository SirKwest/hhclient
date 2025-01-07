package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.repository.FilterRepository

class FilterInteractorImpl(private val filterRepository: FilterRepository) : FilterInteractor {
    override fun saveFilter(filter: Filter) {
        filterRepository.saveFilter(filter)
    }

    override fun saveArea(area: Area) {
        filterRepository.saveArea(area)
    }

    override fun saveIndustry(industry: Industry) {
        filterRepository.saveIndustry(industry)
    }

    override fun getFilter(): Filter {
        return filterRepository.getFilter()
    }

    override fun getArea(): Area {
        return filterRepository.getArea()
    }

    override fun getIndustry(): Industry {
        return filterRepository.getIndustry()
    }

    override fun updateFilter(filter: Filter) {
        filterRepository.updateFilter(filter)
    }

    override fun isFiltersSaved(): Boolean {
        return filterRepository.isFiltersSaved()
    }

    override fun savedFromApplyButton(isApply: Boolean) {
        filterRepository.savedFromApplyButton(isApply)
    }

    override fun getFromApply(): Boolean {
        return filterRepository.getFromApply()
    }
}
