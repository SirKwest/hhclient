package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.FavoriteInteractor
import ru.practicum.android.diploma.domain.api.SharingInteractor
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.impl.FavoriteInteractorImpl
import ru.practicum.android.diploma.domain.impl.SharingInteractorImpl
import ru.practicum.android.diploma.domain.impl.VacancyInteractorImpl

val interactorModule = module {
    single<VacancyInteractor> {
        VacancyInteractorImpl(repository = get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(sharingRepository = get())
    }

    single<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }
}
