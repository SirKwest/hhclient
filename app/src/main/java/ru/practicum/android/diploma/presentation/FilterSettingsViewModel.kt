package ru.practicum.android.diploma.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.models.Filter

class FilterSettingsViewModel(
    private val filterInteractor: FilterInteractor,
) : ViewModel() {
    private val screenState = MutableLiveData<FilterSettingsFragmentState>()

    private val salaryValueState = MutableLiveData<String>()
    private val applyButtonState: MutableLiveData<Boolean> = MutableLiveData(false)
    private val resetButtonState: MutableLiveData<Boolean> = MutableLiveData(false)
    fun observeScreenState(): LiveData<FilterSettingsFragmentState> = screenState
    fun observeSalaryValueState(): LiveData<String> = salaryValueState
    fun observeApplyButtonState(): LiveData<Boolean> = applyButtonState
    fun observeResetButtonState(): LiveData<Boolean> = resetButtonState

    fun updateSalaryValue(value: String) {
        salaryValueState.postValue(value)
        val newSalary: Int? = if (value.isBlank()) {
            null
        } else {
            value.toInt()
        }
        val newFilterData = screenState.value?.filterSettings?.copy(salary = newSalary)
        if (newFilterData != null) {
            saveFilter(newFilterData)
            resetButtonState.postValue(newFilterData != Filter())
        } else {
            resetButtonState.postValue(value.isNotEmpty())
            updateFilter(Filter(salary = value.toInt()))
        }
    }

    fun updateOnlyWithSalaryValue(value: Boolean) {
        val newFilterData = screenState.value?.filterSettings?.copy(isExistSalary = value)
        if (newFilterData != null) {
            saveFilter(newFilterData)
            resetButtonState.postValue(newFilterData != Filter())
        } else {
            resetButtonState.postValue(value)
            updateFilter(Filter(isExistSalary = value))
        }
        //applyButtonState.postValue(newFilterData != screenState.value?.filterSettings)
    }

    fun getFilter() {
        val newFilterData = filterInteractor.getFilter()
        if (screenState.value?.filterSettings != null
            && filterInteractor.isFiltersSaved()
            && newFilterData != screenState.value!!.filterSettings
        ) {
            applyButtonState.postValue(true)
        }
        screenState.postValue(FilterSettingsFragmentState(filterInteractor.getFilter()))
    }

    fun updateFilter(filter: Filter) {
        applyButtonState.postValue(true)
        filterInteractor.updateFilter(filter)
    }

    fun resetFilters() {
        saveFilter(Filter())
        getFilter()
    }

    private fun saveFilter(filter: Filter) {
        filterInteractor.saveFilter(filter)
    }
}
