package ru.practicum.android.diploma.data.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.practicum.android.diploma.data.impl.SharedPreferencesConstant.FILTER_KEY
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.repository.FilterRepository

class FilterRepositoryImpl(private val sharedPreferences: SharedPreferences) : FilterRepository {
    override fun saveFilter(filter: Filter) {
        sharedPreferences.edit().putString(FILTER_KEY, Gson().toJson(filter)).apply()
    }

    override fun getFilter(): Filter {
        val filter = Gson().fromJson(sharedPreferences.getString(FILTER_KEY, null), Filter::class.java) ?: Filter()
        return filter
    }

    override fun updateFilter(filter: Filter) {
        var newFilter = getFilter()
        if (filter.industry != null) {
            newFilter = if (filter.industry == Industry()) {
                newFilter.copy(industry = null)
            } else {
                newFilter.copy(industry = filter.industry)
            }
        }
        if (filter.workPlace != null) {
            newFilter = if (filter.workPlace == Area()) {
                newFilter.copy(workPlace = null)
            } else {
                newFilter.copy(workPlace = filter.workPlace)
            }
        }
        if (filter.salary != null) {
            newFilter = if (filter.salary == -1) {
                newFilter.copy(salary = null)
            } else {
                newFilter.copy(salary = filter.salary)
            }
        }
        if (filter.isExistSalary != null) {
            newFilter = newFilter.copy(isExistSalary = filter.isExistSalary)
        }
        saveFilter(newFilter)
    }

    override fun isFiltersSaved(): Boolean {
        val filter = getFilter()
        val isSalarySaved = filter.salary != null
        val isOnlyWithSalaryCheckSaved = filter.isExistSalary == true
        val isRegionSaved = filter.workPlace != null
        val isIndustrySaved = filter.industry != null
        return isSalarySaved || isOnlyWithSalaryCheckSaved || isRegionSaved || isIndustrySaved
    }

}
