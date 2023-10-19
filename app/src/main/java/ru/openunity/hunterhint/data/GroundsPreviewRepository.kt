package ru.openunity.hunterhint.data

import ru.openunity.hunterhint.models.GroundsPhoto
import ru.openunity.hunterhint.network.ApiService

interface PreviewsRepository {
    suspend fun getPreviewsByGroundsId(id: Int): List<GroundsPhoto>
}

class NetworkPreviewsRepository(
    private val apiService: ApiService
) : PreviewsRepository {
    override suspend fun getPreviewsByGroundsId(id: Int): List<GroundsPhoto> = apiService.getPreviewsByGroundsId(id)
}
