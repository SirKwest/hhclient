package ru.practicum.android.diploma.util

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.Serializable

/**
 * Функция для получения сериализуемых объектов
 */
inline fun <reified T : Serializable> Bundle.getSerializableData(key: String): T? = when {
    SDK_INT >= 33 -> getSerializable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializable(key) as? T
}

/**
 * Функция для конвертации dp в пиксели
 */
fun Float.dpToPx(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics
    ).toInt()
}

/**
 * Функция-расширение для реализации debounce в Fragment.
 *
 * @param isClickAllowedProvider Функция, возвращающая текущее состояние возможности клика.
 * @param onUpdateClickAllowed Функция-обработчик, обновляющая состояние клика (доступен/недоступен).
 * @return `true`, если клик был разрешён в момент вызова, иначе `false`.
 */
fun Fragment.clickDebounce(
    isClickAllowedProvider: () -> Boolean,
    onUpdateClickAllowed: (Boolean) -> Unit
): Boolean {
    val current = isClickAllowedProvider()
    if (current) {
        onUpdateClickAllowed(false)
        lifecycleScope.launch {
            delay(300L) // задержка
            onUpdateClickAllowed(true)
        }
    }
    return current
}
