package ru.openunity.hunterhint.network

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import ru.openunity.hunterhint.dto.BookingDto

interface BookingRetrofitService {
    @POST("booking/new")
    suspend fun createBooking(@Header("Authorization") token: String, @Body booking: BookingDto): Long
}