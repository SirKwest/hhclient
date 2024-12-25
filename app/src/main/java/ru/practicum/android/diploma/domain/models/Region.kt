package ru.practicum.android.diploma.domain.models

data class Region(
    override val id: String,
    override val name: String,
    val parentId: String?,
    val regions: List<Region>
) : Location(id, name)
