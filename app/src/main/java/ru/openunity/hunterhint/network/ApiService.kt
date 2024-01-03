package ru.openunity.hunterhint.network

import retrofit2.http.GET
import retrofit2.http.Path
import ru.openunity.hunterhint.models.GroundsCard

interface ApiService {
    @GET("preview/{groundIds}")
    suspend fun getListOfGroundsPreview(@Path("groundIds") groundIds: String): List<GroundsCard>
    @GET("ground/get-all")
    suspend fun getIdsOfAllGrounds(): List<Int>
}
