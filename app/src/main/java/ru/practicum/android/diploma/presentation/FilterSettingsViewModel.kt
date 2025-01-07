package ru.practicum.android.diploma.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Industry

class FilterSettingsViewModel(
    private val filterInteractor: FilterInteractor,
) : ViewModel() {
    private var currentFilter = Filter()
    private val screenState = MutableLiveData<FilterSettingsFragmentState>()

    private val salaryValueState = MutableLiveData<String>()
    fun observeScreenState(): LiveData<FilterSettingsFragmentState> = screenState
    fun observeSalaryValueState(): LiveData<String> = salaryValueState

    init {
        getFilter()
    }

    fun initializeData() {
        val newArea = filterInteractor.getArea()
        val newIndustry = filterInteractor.getIndustry()
        if (newArea != Area()) {
            currentFilter = currentFilter.copy(workPlace = newArea)
        }
        if (newIndustry != Industry()) {
            currentFilter = currentFilter.copy(industry = newIndustry)
        }
        screenState.postValue(FilterSettingsFragmentState(currentFilter))
    }

    fun updateSalaryValue(value: String) {
        currentFilter = if (value.isNotBlank()) {
            currentFilter.copy(salary = value.toInt())
        } else {
            currentFilter.copy(salary = null)
        }
        salaryValueState.postValue(value)
    }

    fun clearSalaryValue() {
        currentFilter = currentFilter.copy(salary = null)
        screenState.postValue(FilterSettingsFragmentState(currentFilter))
    }

    fun updateOnlyWithSalaryValue(value: Boolean) {
        currentFilter = currentFilter.copy(isExistSalary = value.takeIf { it })
        screenState.postValue(FilterSettingsFragmentState(currentFilter))
    }

    private fun getFilter() {
        currentFilter = filterInteractor.getFilter()
        screenState.postValue(FilterSettingsFragmentState(currentFilter))
    }

    fun resetFilters() {
        filterInteractor.saveFilter(Filter())
    }

    fun saveFilter() {
        filterInteractor.saveFilter(currentFilter)
    }

    fun resetArea() {
        currentFilter = currentFilter.copy(workPlace = null)
        filterInteractor.saveArea(Area())
        screenState.postValue(FilterSettingsFragmentState(currentFilter))
    }

    fun resetIndustry() {
        currentFilter = currentFilter.copy(industry = null)
        filterInteractor.saveIndustry(Industry())
        screenState.postValue(FilterSettingsFragmentState(currentFilter))
    }
}
