package ru.test.playerexo.model.remote

import ru.test.playerexo.model.ui.CurrentUI

data class CurrentDTO(
    val timestart: Long,
    val timestop: Long,
    val title: String,
    val desc: String,
    val cdnvideo: Int,
    val rating: Int
) {

    fun toCurrent() = CurrentUI(
        timeStart = timestart,
        timeStop = timestop,
        title = title,
        desc = desc,
        cdnVideo = cdnvideo,
        rating = rating
    )
}
