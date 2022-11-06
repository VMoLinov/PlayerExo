package ru.test.playerexo.ui.main.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.test.playerexo.model.ui.ChannelUI

object DiffUtil : DiffUtil.ItemCallback<ChannelUI>() {

    override fun areItemsTheSame(oldItem: ChannelUI, newItem: ChannelUI): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ChannelUI, newItem: ChannelUI): Boolean {
        return oldItem.id == newItem.id
    }
}
