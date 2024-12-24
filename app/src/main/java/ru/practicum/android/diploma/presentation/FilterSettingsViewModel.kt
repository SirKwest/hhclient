package ru.practicum.android.diploma.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.models.Filter

class FilterSettingsViewModel(private val filterInteractor: FilterInteractor) : ViewModel() {
    private val screenState = MutableLiveData<FilterSettingsFragmentState>()
    fun observeScreenState(): LiveData<FilterSettingsFragmentState> = screenState

    fun getFilter() {
        screenState.postValue(FilterSettingsFragmentState.SavedFilter(filterInteractor.getFilter()))
    }

    fun saveFilter(filter: Filter) {
        filterInteractor.saveFilter(filter)
    }
}
