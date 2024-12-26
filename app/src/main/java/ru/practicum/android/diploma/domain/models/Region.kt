package ru.practicum.android.diploma.domain.models

import java.io.Serializable

data class Region(
    override val id: String,
    override val name: String,
    val parentId: String?,
    val regions: List<Region>
) : Location(id, name), Serializable
