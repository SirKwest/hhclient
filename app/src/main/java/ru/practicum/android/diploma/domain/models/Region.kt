package ru.practicum.android.diploma.domain.models

data class Region(
    val id: String,
    val name: String,
    val parentId: String?,
    val regions: List<Region>
)
