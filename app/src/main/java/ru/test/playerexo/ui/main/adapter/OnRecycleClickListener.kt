package ru.test.playerexo.ui.main.adapter

import ru.test.playerexo.model.ui.ChannelUI

interface OnRecycleClickListener {
    fun channelClick(channel: ChannelUI)
    fun favoriteClick(channel: ChannelUI)
}
