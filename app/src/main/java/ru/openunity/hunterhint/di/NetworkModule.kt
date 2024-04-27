package ru.openunity.hunterhint.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.openunity.hunterhint.network.GroundRetrofitService
import ru.openunity.hunterhint.network.UserRetrofitService
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideBaseUrl(): String = "http://beydgek.com/"

    @Singleton
    @Provides
    fun provideUserRetrofitService(retrofit: Retrofit): UserRetrofitService {
        return retrofit
            .create(UserRetrofitService::class.java)
    }

    @Singleton
    @Provides
    fun provideGroundRetrofitService(retrofit: Retrofit): GroundRetrofitService {
        return retrofit
            .create(GroundRetrofitService::class.java)
    }

    @Singleton
    @Provides
    fun getRetrofit(baseUrl: String): Retrofit {
        val timeoutInSecond: Long = 10
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(timeoutInSecond, TimeUnit.SECONDS)
            .connectTimeout(timeoutInSecond, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
}