package ru.test.playerexo.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.test.playerexo.R
import ru.test.playerexo.databinding.ItemRecyclerBinding
import ru.test.playerexo.model.ui.ChannelUI

class ChannelsAdapter(val listener: OnRecycleClickListener) :
    ListAdapter<ChannelUI, ChannelsAdapter.ViewHolder>(DiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRecyclerBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ViewHolder(private val binding: ItemRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(channels: ChannelUI) = with(binding) {
            textHeader.text = channels.nameRu
            textProgram.text = channels.current.title
            Glide.with(itemView).load(channels.image).transform(RoundedCorners(4)).into(image)
            DrawableCompat.setTint(
                favorites.drawable, ContextCompat.getColor(
                    favorites.context,
                    if (!channels.isFavorite) R.color.fav_false
                    else R.color.fav_true
                )
            )
            itemView.setOnClickListener {
                listener.channelClick(currentList[absoluteAdapterPosition])
            }
            favorites.setOnClickListener {
                listener.favoriteClick(currentList[absoluteAdapterPosition])
            }
        }
    }
}
