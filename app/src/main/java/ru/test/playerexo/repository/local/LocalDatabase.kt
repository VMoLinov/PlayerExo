package ru.test.playerexo.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.test.playerexo.model.local.ChannelDB

@Database(entities = [ChannelDB::class], version = 2, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun localSource(): LocalSource
}
