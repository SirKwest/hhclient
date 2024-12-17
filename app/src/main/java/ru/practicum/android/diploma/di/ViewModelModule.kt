package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.SearchFragmentViewModel
import ru.practicum.android.diploma.presentation.VacancyDetailsViewModel

val viewModelModule = module {
    viewModelOf(::SearchFragmentViewModel)
    viewModelOf(::VacancyDetailsViewModel)
}
