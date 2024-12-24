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
        //save to cache
        applyButtonState.postValue(true) // true -> is cache different from saved
        resetButtonState.postValue(value.isNotEmpty()) // value.isNotEmpty -> is any data currently set
    }

    fun updateOnlyWithSalaryValue(value: Boolean) {
        //save to cache
        applyButtonState.postValue(true) // true -> is cache different from saved
        resetButtonState.postValue(value) // value.isNotEmpty -> is any data currently set
    }

    fun getFilter() {
        screenState.postValue(FilterSettingsFragmentState(filterInteractor.getFilter()))
    }

    fun saveFilter(filter: Filter) {
        filterInteractor.saveFilter(filter)
    }

    fun resetFilters() {
        saveFilter(Filter(null, null, null, false))
        getFilter()
    }
}
