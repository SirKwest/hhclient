package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.data.impl.FavoriteRepositoryImpl
import ru.practicum.android.diploma.data.impl.SharingRepositoryImpl
import ru.practicum.android.diploma.data.impl.VacancyRepositoryImpl
import ru.practicum.android.diploma.domain.repository.FavoriteRepository
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
}
