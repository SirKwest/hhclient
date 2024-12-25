package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.repository.FilterRepository

class FilterInteractorImpl(private val filterRepository: FilterRepository) : FilterInteractor {
    override fun saveFilter(filter: Filter) {
        filterRepository.saveFilter(filter)
    }

    override fun getFilter(): Filter {
        return filterRepository.getFilter()
    }

    override fun updateFilter(filter: Filter) {
        filterRepository.updateFilter(filter)
    }

    override fun isFiltersSaved(): Boolean {
        return filterRepository.isFiltersSaved()
    }
}
