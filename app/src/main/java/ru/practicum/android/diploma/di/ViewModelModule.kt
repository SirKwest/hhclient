package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.CountriesViewModel
import ru.practicum.android.diploma.presentation.FavoritesViewModel
import ru.practicum.android.diploma.presentation.FilterSettingsViewModel
import ru.practicum.android.diploma.presentation.IndustriesViewModel
import ru.practicum.android.diploma.presentation.RegionsViewModel
import ru.practicum.android.diploma.presentation.SearchFragmentViewModel
import ru.practicum.android.diploma.presentation.VacancyDetailsViewModel
import ru.practicum.android.diploma.presentation.WorkLocationFragmentViewModel

val viewModelModule = module {
    viewModelOf(::SearchFragmentViewModel)
    viewModelOf(::VacancyDetailsViewModel)
    viewModelOf(::FavoritesViewModel)
    viewModelOf(::FilterSettingsViewModel)
    viewModelOf(::IndustriesViewModel)
    viewModelOf(::CountriesViewModel)
    viewModelOf(::WorkLocationFragmentViewModel)
    viewModel<RegionsViewModel> { params ->
        RegionsViewModel(countryId = params.get(), locationInteractor = get())
    }
}
