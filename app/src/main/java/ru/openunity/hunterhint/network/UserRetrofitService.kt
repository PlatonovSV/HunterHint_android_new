package ru.openunity.hunterhint.network

import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.openunity.hunterhint.dto.AuthRequestDto
import ru.openunity.hunterhint.dto.AuthResponseDto
import ru.openunity.hunterhint.dto.UserCardDto
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

    @FormUrlEncoded
    @POST("user/update")
    suspend fun updateRemote(@FieldMap fields: Map<String, String>): Long

    @GET("users/find")
    suspend fun findUsers(
        @Header("Authorization") token: String,
        @Query("name") name: String,
        @Query("lastName") lastName: String
    ): List<Long>

    @POST("users/{id}/status/set")
    suspend fun setUserStatus(
        @Header("Authorization") jwt: String,
        @Path("id") userId: Long,
        @Body status: Int
    ): Long

    @GET("users/{id}/card")
    suspend fun getUserCard(
        @Header("Authorization") token: String = "",
        @Path("id") userId: Long
    ): UserCardDto

    @POST("users/{id}/access/set")
    suspend fun setUserAccessLevel(
        @Header("Authorization") jwt: String,
        @Path("id") userId: Long,
        @Body accessLevel: Int
    ): Long
}