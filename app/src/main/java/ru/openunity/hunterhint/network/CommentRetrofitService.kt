package ru.openunity.hunterhint.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import ru.openunity.hunterhint.dto.NewReviewDto
import ru.openunity.hunterhint.dto.ReviewDto

interface CommentRetrofitService {
    @POST("review/new")
    suspend fun create(@Header("Authorization") token: String, @Body dto: NewReviewDto): Long

    @GET("review/ground/{groundId}")
    suspend fun getGroundsReview(@Path("groundId") groundId: Int): List<Long>

    @GET("review/{id}")
    suspend fun getReview(@Path("id") id: Long): ReviewDto
}