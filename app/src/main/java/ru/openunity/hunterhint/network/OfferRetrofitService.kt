package ru.openunity.hunterhint.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.openunity.hunterhint.models.HuntingOffer

interface OfferRetrofitService {
    @GET("offer/search")
    suspend fun findOffers(@QueryMap params: Map<String, String>): List<HuntingOffer>
    @GET("offer/{id}")
    suspend fun getOffer(@Path("id") id: Long): HuntingOffer
}