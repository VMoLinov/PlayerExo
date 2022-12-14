package ru.test.playerexo.repository

import androidx.lifecycle.map
import ru.test.playerexo.model.local.ChannelDB
import ru.test.playerexo.model.ui.ChannelUI
import ru.test.playerexo.repository.local.LocalSource
import ru.test.playerexo.repository.network.NetworkSource
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val local: LocalSource,
    private val network: NetworkSource
) : MainRepository {

    override val data = local.liveData.map { it.toChannelsUI() }
    override var favorites = data.map { it.filter { c -> c.isFavorite } }

    override suspend fun getData() {
        try {
            val response = network.getData().channels.map { it.toChannelUI() }
            combineSources(response)
            local.insertAll(response.map { it.toChannelDB() })
        } catch (e: Exception) {
            throw e
        }
    }

    private fun combineSources(response: List<ChannelUI>) {
        val favoriteIds = mutableListOf<Int>()
        data.value?.forEach { if (it.isFavorite) favoriteIds.add(it.id) }
        response.map { if (favoriteIds.contains(it.id)) it.isFavorite = true }
    }

    override suspend fun update(channel: ChannelUI) {
        local.update(channel.toChannelDB())
    }

    private fun List<ChannelDB>.toChannelsUI(): List<ChannelUI> = map { it.toChannelUI() }
}
