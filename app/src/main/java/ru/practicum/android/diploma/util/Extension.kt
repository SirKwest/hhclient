package ru.practicum.android.diploma.util

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.Serializable

/**
 * Функция для получения сериализуемых объектов
 */
inline fun <reified T : Serializable> Bundle.getSerializableData(key: String): T? = when {
    SDK_INT >= TIRAMISU ->
        getSerializable(key, T::class.java)
    else ->
        @Suppress("DEPRECATION")
        getSerializable(key) as? T
}

/**
 * Функция-расширение для защиты от повторного запуска обработчика клика в фрагменте
 *
 * @param requiredDelay  Время необходимой задержки между событиями клика
 * @param coroutineScope Контекст в котором вызывается
 * @param action         Обработчик клика
 */
fun <T> debouncedAction(
    requiredDelay: Long,
    coroutineScope: CoroutineScope,
    action: (T) -> Unit
): (T) -> Unit {
    var job: Job? = null
    return { param: T ->
        job?.cancel()
        if (job?.isCompleted != false) {
            job = coroutineScope.launch {
                delay(requiredDelay)
                action(param)
            }
        }
    }
}
