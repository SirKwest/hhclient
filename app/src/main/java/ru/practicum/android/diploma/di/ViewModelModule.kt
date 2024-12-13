package ru.practicum.android.diploma.di

import org.koin.dsl.module
import org.koin.core.module.dsl.viewModelOf
import ru.practicum.android.diploma.presentation.SearchFragmentViewModel

val viewModelModule = module {
    viewModelOf(::SearchFragmentViewModel)
}
