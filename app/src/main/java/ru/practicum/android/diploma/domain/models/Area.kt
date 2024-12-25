package ru.practicum.android.diploma.domain.models

data class Area(
    val regionId: String,
    val countryName: String,
    val regionName: String,
)

fun Area.displayName(): String {
    return "$countryName, $regionName"
}
