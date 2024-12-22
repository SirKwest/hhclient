package ru.practicum.android.diploma.data.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.practicum.android.diploma.data.impl.SharedPreferencesConstant.FILTER_KEY
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.repository.FilterRepository

class FilterRepositoryImpl(private val sharedPreferences: SharedPreferences) : FilterRepository {
    override fun saveFilter(filter: Filter) {
        sharedPreferences.edit().putString(FILTER_KEY, Gson().toJson(filter)).apply()
    }

    override fun getFilter(): Filter {
        val filter = Gson().fromJson(sharedPreferences.getString(FILTER_KEY, null), Filter::class.java)
        return filter
    }

}
