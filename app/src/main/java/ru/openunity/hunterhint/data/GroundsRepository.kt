package ru.openunity.hunterhint.data

import ru.openunity.hunterhint.models.GroundsCard
import ru.openunity.hunterhint.network.ApiService

interface GroundsRepository {
    suspend fun getListOfGroundsPreview(groundIds: String): List<GroundsCard>
    suspend fun getIdsOfAllGrounds(): List<Int>
}

class NetworkGroundsRepository(
    private val apiService: ApiService
) : GroundsRepository {
    override suspend fun getListOfGroundsPreview(groundIds: String): List<GroundsCard> =
        apiService.getListOfGroundsPreview(groundIds)

    override suspend fun getIdsOfAllGrounds(): List<Int> = apiService.getIdsOfAllGrounds()
}
