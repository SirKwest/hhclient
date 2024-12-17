package ru.practicum.android.diploma.util

import android.content.Context
import ru.practicum.android.diploma.R
import java.text.NumberFormat
import java.util.Locale

class SalaryDescriptionBuilder(private val context: Context) {
    fun build(
        min: Int?,
        max: Int?,
        code: String?,
    ): String {
        val result: String
        if (min == null && max == null) {
            result = context.getString(R.string.no_salary)
        } else if (min == null) {
            result = context.getString(
                R.string.max_salary,
                NumberFormat.getInstance(Locale("RU")).format(max),
                getCurrencySymbol(code),
            )
        } else if (max == null) {
            result = context.getString(
                R.string.min_salary,
                NumberFormat.getInstance(Locale("RU")).format(min),
                getCurrencySymbol(code),
            )
        } else {
            result = context.getString(
                R.string.full_salary,
                NumberFormat.getInstance(Locale("RU")).format(min),
                getCurrencySymbol(code),
                NumberFormat.getInstance(Locale("RU")).format(max),
            )
        }
        return result
    }

    private fun getCurrencySymbol(code: String?): String {
        return when (code) {
            "RUB", "RUR" -> "₽"
            "BYR" -> "Br"
            "USD" -> "$"
            "EUR" -> "€"
            "KZT" -> "₸"
            "UAH" -> "₴"
            "AZN" -> "₼"
            "UZS" -> "сўм"
            "GEL" -> "₾"
            "KGT" -> "сом"
            else -> code ?: "?"
        }
    }
}
