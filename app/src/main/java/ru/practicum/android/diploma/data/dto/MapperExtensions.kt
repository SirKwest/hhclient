package ru.practicum.android.diploma.data.dto

import ru.practicum.android.diploma.data.dto.response.VacancyByIdResponse
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyShort

fun VacancyShortDto.toVacancyShort(): VacancyShort {
    return VacancyShort(
        id = id,
        name = name,
        location = area.name,
        employer = employer.name,
        logo = employer.logo?.original ?: (employer.logo?.big ?: employer.logo?.small).orEmpty(),
        salaryLow = salary?.low,
        salaryHigh = salary?.high,
        currency = salary?.currency,
    )
}

fun VacancyByIdResponse.toVacancy(isFavorite: Boolean): Vacancy {
    return Vacancy(
        id = id,
        name = name,
        area = area.name,
        employer = employer.name,
        logo = employer.logo?.original ?: (employer.logo?.big ?: employer.logo?.small),
        salaryLow = salary?.low,
        salaryHigh = salary?.high,
        currency = salary?.currency,
        keySkills = keySkills.map { it.name },
        description = description,
        experience = experience?.name,
        employment = employment?.name,
        schedule = schedule?.name,
        url = url,
        isFavorite = isFavorite
    )
}
