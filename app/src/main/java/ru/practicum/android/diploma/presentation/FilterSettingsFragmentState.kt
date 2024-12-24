package ru.practicum.android.diploma.presentation

import ru.practicum.android.diploma.domain.models.Filter

sealed interface FilterSettingsFragmentState {
    data class SavedFilter(val filter: Filter?) : FilterSettingsFragmentState
}
