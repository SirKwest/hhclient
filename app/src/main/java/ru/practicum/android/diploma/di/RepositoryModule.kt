package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.data.impl.FavoriteRepositoryImpl

import ru.practicum.android.diploma.data.impl.FilterRepositoryImpl

import ru.practicum.android.diploma.data.impl.IndustriesRepositoryImpl

import ru.practicum.android.diploma.data.impl.LocationRepositoryImpl
import ru.practicum.android.diploma.data.impl.SharingRepositoryImpl
import ru.practicum.android.diploma.data.impl.VacancyRepositoryImpl
import ru.practicum.android.diploma.domain.repository.FavoriteRepository

import ru.practicum.android.diploma.domain.repository.FilterRepository

import ru.practicum.android.diploma.domain.repository.IndustriesRepository

import ru.practicum.android.diploma.domain.repository.LocationRepository
import ru.practicum.android.diploma.domain.repository.SharingRepository
import ru.practicum.android.diploma.domain.repository.VacancyRepository

val repositoryModule = module {
    single<VacancyRepository> {
        VacancyRepositoryImpl(headhunterClient = get(), get())
    }

    single<SharingRepository> {
        SharingRepositoryImpl(androidContext())
    }

    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get(), get())
    }

    single<FilterRepository> {
        FilterRepositoryImpl(get())
    }

    single<IndustriesRepository> {
        IndustriesRepositoryImpl(headhunterClient = get())
    }

    single<LocationRepository> {
        LocationRepositoryImpl(headhunterClient = get())
    }
}
