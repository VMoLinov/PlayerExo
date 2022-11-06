package ru.test.playerexo.model.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrentUI(
    val timeStart: Long,
    val timeStop: Long,
    val title: String,
    val desc: String,
    val cdnVideo: Int,
    val rating: Int
) : Parcelable
