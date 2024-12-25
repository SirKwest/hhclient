package ru.practicum.android.diploma.domain.models

import java.io.Serializable

abstract class Location(
    open val id: String,
    open val name: String
)

data class Country(
    override val id: String,
    override val name: String
) : Location(id, name), Serializable


data class Region(
    override val id: String,
    override val name: String,
    val parentId: String?,
    val regions: List<Region>
) : Location(id, name)
