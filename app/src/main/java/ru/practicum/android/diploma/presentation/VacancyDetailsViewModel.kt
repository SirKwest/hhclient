package ru.practicum.android.diploma.presentation

import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.api.SharingInteractor

class VacancyDetailsViewModel(
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    fun shareVacancy() {
        sharingInteractor.shareText("stub")
    }

    fun openEmail() {
        sharingInteractor.openEmail("stub")
    }

    fun dial() {
        sharingInteractor.dial("stub")
    }
}
