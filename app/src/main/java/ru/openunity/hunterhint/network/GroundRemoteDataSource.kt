package ru.openunity.hunterhint.network

import retrofit2.http.GET
import retrofit2.http.Path
import ru.openunity.hunterhint.models.Ground
import ru.openunity.hunterhint.models.GroundsCard

interface GroundRemoteDataSource {
    @GET("preview/{groundIds}")
    suspend fun getListOfGroundsPreview(@Path("groundIds") groundIds: String): List<GroundsCard>

    @GET("ground/get-all")
    suspend fun getIdsOfAllGrounds(): List<Int>

    @GET("ground/{groundId}")
    suspend fun getGround(@Path("groundId") groundId: Int): Ground

}