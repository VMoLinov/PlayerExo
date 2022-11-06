package ru.test.playerexo.model.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.test.playerexo.model.ui.ChannelUI
import ru.test.playerexo.model.ui.CurrentUI

@Entity
data class ChannelDB(
    @PrimaryKey
    val id: Int,
    val epgId: Int,
    val nameRu: String,
    val nameEn: String,
    val vitrinaEventsUrl: String,
    val isFederal: Boolean,
    val address: String,
    val cdn: String,
    val url: String,
    val urlSound: String,
    val image: String,
    val hasEpg: Boolean,
    @Embedded
    val current: CurrentUI,
    val regionCode: Int,
    val tz: Int,
    val isForeign: Boolean,
    val number: Int,
    val drmStatus: Int,
    val owner: String,
    val foreignPlayerKey: Boolean,
    var isFavorite: Boolean
) {

    fun toChannelUI() = ChannelUI(
        id = id,
        epgId = epgId,
        nameRu = nameRu,
        nameEn = nameEn,
        vitrinaEventsUrl = vitrinaEventsUrl,
        isFederal = isFederal,
        address = address,
        cdn = cdn,
        url = url,
        urlSound = urlSound,
        image = image,
        hasEpg = hasEpg,
        current = current,
        regionCode = regionCode,
        tz = tz,
        isForeign = isForeign,
        number = number,
        drmStatus = drmStatus,
        owner = owner,
        foreignPlayerKey = foreignPlayerKey,
        isFavorite = isFavorite
    )
}
