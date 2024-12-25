package ru.practicum.android.diploma.domain.models

data class Area(
    val regionId: String? = null,
    val countryName: String? = null,
    val regionName: String? = null,
)

fun Area.displayName(): String {
    return "$countryName, $regionName"
}
