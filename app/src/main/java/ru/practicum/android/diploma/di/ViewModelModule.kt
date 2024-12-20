package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.SearchFragmentViewModel
import ru.practicum.android.diploma.presentation.VacancyDetailsViewModel
import ru.practicum.android.diploma.presentation.FavoritesViewModel

val viewModelModule = module {
    viewModelOf(::SearchFragmentViewModel)
    viewModelOf(::VacancyDetailsViewModel)
    viewModelOf(::FavoritesViewModel)
}
