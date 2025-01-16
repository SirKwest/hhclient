package ru.practicum.android.diploma.domain.models

data class SearchOptions(
    val text: String,
    val page: Int,
    val filter: Filter
)

fun SearchOptions.toQueryMap(): Map<String, String> =
    buildMap {
        put("text", text)
        put("page", page.toString())
        if (filter.isExistSalary == true) {
            put("only_with_salary", "true")
        }
        if (filter.salary != null) {
            put("salary", filter.salary.toString())
        }
        if (filter.industry != null) {
            put("industry", filter.industry!!.id.toString())
        }
        if (filter.workPlace != null) {
            put("area", filter.workPlace!!.regionId ?: filter.workPlace!!.countryId.toString())
        }
    }
