package ru.test.playerexo.model.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.test.playerexo.model.local.ChannelDB

@Parcelize
data class ChannelUI(
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
    val current: CurrentUI,
    val regionCode: Int,
    val tz: Int,
    val isForeign: Boolean,
    val number: Int,
    val drmStatus: Int,
    val owner: String,
    val foreignPlayerKey: Boolean,
    var isFavorite: Boolean
) : Parcelable {

    fun toChannelDB() = ChannelDB(
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
