package ru.practicum.android.diploma.data.impl

import android.content.Context
import android.content.Intent
import ru.practicum.android.diploma.domain.repository.SharingRepository

class SharingRepositoryImpl(
    private val context: Context,
) : SharingRepository {

    override fun shareText(text: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
