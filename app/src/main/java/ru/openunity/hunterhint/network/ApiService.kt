package ru.openunity.hunterhint.network

import ru.openunity.hunterhint.models.GroundsPhoto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("")
    suspend fun getPreviewsByGroundsId(@Path("Id") id: Int): List<GroundsPhoto>
}
