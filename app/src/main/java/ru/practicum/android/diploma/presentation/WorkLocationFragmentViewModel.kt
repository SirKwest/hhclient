package ru.practicum.android.diploma.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Region

class WorkLocationFragmentViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {
    private var countryValue: Country? = null
    private var regionValue: Region? = null

    private val applyButtonState = MutableLiveData(false)
    fun observeApplyButtonState(): LiveData<Boolean> = applyButtonState

    var isSelectedRegionShouldBeRemoved = false
        private set

    init {
        filterInteractor.getArea().let { area ->
            if (area.countryId != null && area.countryName != null) {
                countryValue = Country(id = area.countryId, name = area.countryName)
            }

            if (area.regionId != null && area.regionName != null) {
                regionValue = Region(id = area.regionId, name = area.regionName, parentId = null, regions = emptyList())
            }
        }
        updateApplyButtonState()
    }

    fun getArea() = filterInteractor.getArea()

    fun setCountryValue(value: Country?) {
        if (value != null && countryValue?.id != value.id) {
            isSelectedRegionShouldBeRemoved = true
            regionValue = null
        } else {
            isSelectedRegionShouldBeRemoved = false
        }
        countryValue = value
        updateApplyButtonState()
    }

    fun getCountryValue(): Country? {
        return countryValue
    }

    fun setRegionValue(value: Region?) {
        regionValue = value
        updateApplyButtonState()
    }

    fun saveAreaToFilter() {
        filterInteractor.saveArea(
            Area(
                regionId = regionValue?.id,
                countryId = countryValue?.id,
                countryName = countryValue?.name,
                regionName = regionValue?.name,
            )
        )
    }

    private fun updateApplyButtonState() {
        applyButtonState.postValue(countryValue != null || regionValue != null)
    }
}
