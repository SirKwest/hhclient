package ru.practicum.android.diploma.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.IndustryResource

interface IndustriesRepository {
    fun getIndustries(): Flow<IndustryResource>
}
