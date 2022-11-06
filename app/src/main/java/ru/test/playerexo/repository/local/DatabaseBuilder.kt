package ru.test.playerexo.repository.local

import android.content.Context
import androidx.room.Room

object DatabaseBuilder {

    private const val DB_NAME = "channels"

    @Volatile
    private var INSTANCE: LocalDatabase? = null

    fun getDatabase(context: Context): LocalDatabase =
        INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also {
                INSTANCE = it
            }
        }

    private fun buildDatabase(context: Context) =
        Room.databaseBuilder(
            context.applicationContext, LocalDatabase::class.java, DB_NAME
        ).fallbackToDestructiveMigration().build()
}
