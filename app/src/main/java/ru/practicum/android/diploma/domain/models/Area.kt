package ru.practicum.android.diploma.domain.models

data class Area(
    val regionId: String? = null,
    val countryName: String? = null,
    val regionName: String? = null,
)

fun Area.displayName(): String {
    return if (countryName != null && regionName == null) {
        countryName
    } else if (countryName == null && regionName != null) {
        regionName
    } else {
        "$countryName, $regionName"
    }
}
