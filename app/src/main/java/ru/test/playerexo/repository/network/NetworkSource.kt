package ru.test.playerexo.repository.network

import retrofit2.http.GET
import ru.test.playerexo.model.remote.ResponseDTO

interface NetworkSource {

    @GET("channels.json")
    suspend fun getData(): ResponseDTO
}
