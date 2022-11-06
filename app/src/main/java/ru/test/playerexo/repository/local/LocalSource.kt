package ru.test.playerexo.repository.local

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.test.playerexo.model.local.ChannelDB

@Dao
interface LocalSource {

    @get:Query("SELECT * FROM channeldb")
    val liveData: LiveData<List<ChannelDB>>

    @Insert
    suspend fun insert(channel: ChannelDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(channel: List<ChannelDB>)

    @Delete
    suspend fun delete(channel: ChannelDB)

    @Update
    suspend fun update(channel: ChannelDB)
}
