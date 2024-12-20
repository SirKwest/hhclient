package ru.practicum.android.diploma.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.data.model.VacancyEntityDB

@Database(
    version = 1,
    entities = [VacancyEntityDB::class]
)
abstract class AppDatabase : RoomDatabase()
