package ru.openunity.hunterhint.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import ru.openunity.hunterhint.dto.AuthRequestDto
import ru.openunity.hunterhint.dto.AuthResponseDto
import ru.openunity.hunterhint.dto.UserDto
import ru.openunity.hunterhint.dto.UserRegDto

interface UserRetrofitService {
    @POST("users/new")
    suspend fun createUser(@Body user: UserRegDto): Long

    @GET("users/is-phone-stored")
    suspend fun isPhoneStored(
        @Query("phone") phone: String,
        @Query("country") countryCode: Int
    ): Boolean

    @GET("users/is-email-stored")
    suspend fun isEmailStored(@Query("email") email: String): Boolean

    @POST("users/auth")
    suspend fun authorization(@Body auth: AuthRequestDto): AuthResponseDto

    @GET("users/get")
    suspend fun getUsersData(@Header("Authorization") token: String): UserDto
}