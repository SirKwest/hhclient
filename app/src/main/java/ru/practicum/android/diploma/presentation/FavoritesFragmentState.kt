package ru.practicum.android.diploma.presentation

import ru.practicum.android.diploma.domain.models.VacancyShort

sealed interface FavoritesFragmentState {
    data object EmptyResults : FavoritesFragmentState
    data object Error : FavoritesFragmentState

    data class ShowResults(val favorites: List<VacancyShort>) : FavoritesFragmentState
}
