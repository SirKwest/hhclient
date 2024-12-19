package ru.practicum.android.diploma.data.db.converters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.data.db.model.VacancyEntityDB
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyDbConverter {
    fun mapToVacancyEntity(vacancy: Vacancy): VacancyEntityDB {
        return VacancyEntityDB(
            vacancy.id,
            vacancy.name,
            vacancy.area,
            vacancy.employer,
            vacancy.logo,
            vacancy.salaryLow,
            vacancy.salaryHigh,
            vacancy.currency,
            Gson().toJson(vacancy.keySkills),
            vacancy.description,
            vacancy.experience,
            vacancy.employment,
            vacancy.schedule,
            vacancy.url
        )
    }

    fun mapToVacancy(entity: VacancyEntityDB): Vacancy {
        val listType = object : TypeToken<List<String>>() {}.type
        return Vacancy(
            entity.id,
            entity.name,
            entity.area,
            entity.employer,
            entity.logo,
            entity.salaryLow,
            entity.salaryHigh,
            entity.currency,
            Gson().fromJson(entity.keySkills, listType),
            entity.description,
            entity.experience,
            entity.employment,
            entity.schedule,
            entity.url
        )
    }
}
