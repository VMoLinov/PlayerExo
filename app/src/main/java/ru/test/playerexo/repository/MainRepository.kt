package ru.test.playerexo.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.test.playerexo.app.Resource
import ru.test.playerexo.model.ui.ChannelUI

interface MainRepository {

    val data: LiveData<List<ChannelUI>>
    val favorites: LiveData<List<ChannelUI>>

    suspend fun getData()

    suspend fun update(channel: ChannelUI)
}
