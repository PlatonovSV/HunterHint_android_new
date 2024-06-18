package ru.openunity.hunterhint.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import ru.openunity.hunterhint.dto.BookingCardDto
import ru.openunity.hunterhint.dto.BookingDataDto
import ru.openunity.hunterhint.dto.NewBookingDto
import ru.openunity.hunterhint.dto.UserCardDto

interface BookingRetrofitService {
    @POST("booking/new")
    suspend fun createBooking(
        @Header("Authorization") token: String,
        @Body booking: NewBookingDto
    ): Long

    @GET("booking/all")
    suspend fun getUsersBooking(@Header("Authorization") token: String): List<BookingCardDto>

    @GET("booking/{id}")
    suspend fun getBooking(
        @Header("Authorization") jwt: String,
        @Path("id") id: Long
    ): BookingDataDto

    @GET("booking/clients")
    suspend fun getAllClientsList(@Header("Authorization") jwt: String): List<UserCardDto>
}