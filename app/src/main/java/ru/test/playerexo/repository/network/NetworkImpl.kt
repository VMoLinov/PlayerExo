package ru.test.playerexo.repository.network

import ru.test.playerexo.model.remote.ResponseDTO

class NetworkImpl(private val apiService: NetworkSource) : NetworkSource {

    override suspend fun getData(): ResponseDTO {
        return apiService.getData()
    }
}
