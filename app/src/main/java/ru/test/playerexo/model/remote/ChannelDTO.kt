package ru.test.playerexo.model.remote

import ru.test.playerexo.model.ui.ChannelUI

data class ChannelDTO(
    val id: Int,
    val epg_id: Int,
    val name_ru: String,
    val name_en: String,
    val vitrina_events_url: String,
    val is_federal: Boolean,
    val address: String,
    val cdn: String,
    val url: String,
    val url_sound: String,
    val image: String,
    val hasEpg: Boolean,
    val current: CurrentDTO,
    val region_code: Int,
    val tz: Int,
    val is_foreign: Boolean,
    val number: Int,
    val drm_status: Int,
    val owner: String,
    val foreign_player_key: Boolean
) {

    fun toChannelUI(isFavorite: Boolean = false): ChannelUI =
        ChannelUI(
            id = id,
            epgId = epg_id,
            nameRu = name_ru,
            nameEn = name_en,
            vitrinaEventsUrl = vitrina_events_url,
            isFederal = is_federal,
            address = address,
            cdn = cdn,
            url = url,
            urlSound = url_sound,
            image = image,
            hasEpg = hasEpg,
            current = current.toCurrent(),
            regionCode = region_code,
            tz = tz,
            isForeign = is_foreign,
            number = number,
            drmStatus = drm_status,
            owner = owner,
            foreignPlayerKey = foreign_player_key,
            isFavorite = isFavorite
        )
}
