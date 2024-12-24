package ru.practicum.android.diploma.di

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.db.converters.VacancyDbConverter
import ru.practicum.android.diploma.data.impl.SharedPreferencesConstant
import ru.practicum.android.diploma.data.network.HHApi
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient

val dataModule = module {

    single<NetworkClient> {
        RetrofitNetworkClient(
            api = get(),
            connectivityManager = androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        )
    }

    single<HHApi> {
        Retrofit.Builder()
            .baseUrl("https://api.hh.ru")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HHApi::class.java)
    }

    single<AppDatabase> {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single<VacancyDbConverter> {
        VacancyDbConverter()
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(SharedPreferencesConstant.FILTER_PREF_FILE, Context.MODE_PRIVATE)
    }
}
