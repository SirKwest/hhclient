package ru.practicum.android.diploma.data.db.converters

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
            vacancy.keySkills,
            vacancy.description,
            vacancy.experience,
            vacancy.employment,
            vacancy.schedule,
            vacancy.url
        )
    }
}
