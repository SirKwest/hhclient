package ru.practicum.android.diploma.domain.models

import java.io.Serializable

data class Country(
    override val id: String,
    override val name: String
) : Location(id, name), Serializable
