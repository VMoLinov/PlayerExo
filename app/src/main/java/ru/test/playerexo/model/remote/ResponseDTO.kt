package ru.test.playerexo.model.remote

data class ResponseDTO(
    val channels: List<ChannelDTO>,
    val valid: Long,
    val ckey: String
)
