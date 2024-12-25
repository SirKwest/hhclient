package ru.practicum.android.diploma.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Region

class WorkLocationFragmentViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {
    private var countryValue: Country? = null
    var regionValue: Region? = null

    private val applyButtonState = MutableLiveData(false)

    fun observeApplyButtonState(): MutableLiveData<Boolean> = applyButtonState

    fun setCountryValue(value: Country) {
        countryValue = value
        applyButtonState.postValue(true)
    }

    fun getCountryValue(): Country? {
        return countryValue
    }

    fun isSelectedRegionShouldBeRemoved(): Boolean {
        if (countryValue != null && regionValue != null && regionValue!!.parentId != countryValue!!.id) {
            regionValue = null
            return true
        }
        return false
    }

    fun saveAreaToFilter() {
        filterInteractor.updateFilter(
            Filter(
                workPlace = Area(
                    regionId = regionValue?.id ?: countryValue?.id,
                    countryName = countryValue?.name,
                    regionName = regionValue?.name,
                )
            )
        )
    }
}
