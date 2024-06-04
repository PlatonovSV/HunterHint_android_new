package ru.openunity.hunterhint.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.openunity.hunterhint.dto.DistrictDto
import ru.openunity.hunterhint.dto.GroundsNameDto
import ru.openunity.hunterhint.models.Ground
import ru.openunity.hunterhint.models.GroundsCard


interface GroundRetrofitService {
    @GET("preview")
    suspend fun getListOfGroundsPreview(@Query("groundsId") groundsId: List<Int>): List<GroundsCard>

    @GET("ground/get-all")
    suspend fun getIdsOfAllGrounds(): List<Int>

    @GET("ground/{groundId}")
    suspend fun getGround(@Path("groundId") groundId: Int): Ground

    @GET("districts/{regionCode}")
    suspend fun getDistricts(@Path("regionCode") regionCode: Int): List<DistrictDto>

    @POST("grounds/name")
    suspend fun getGroundsName(@Body userInput: GroundsNameDto): List<GroundsNameDto>
}