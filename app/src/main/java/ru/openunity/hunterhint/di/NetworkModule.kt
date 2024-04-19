package ru.openunity.hunterhint.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.openunity.hunterhint.network.GroundRemoteDataSource
import ru.openunity.hunterhint.network.UserRetrofitService
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

// @Module informs Dagger that this class is a Dagger Module
@Module
class NetworkModule {
    @Singleton
    // @Provides tell Dagger how to create instances of the type that this function
    // Function parameters are the dependencies of this type.
    @Provides
    fun provideLoginRetrofitService(retrofit: Retrofit): UserRetrofitService {

        // Whenever Dagger needs to provide an instance,
        // this code (the one inside the @Provides method) is run.
        return retrofit
            .create(UserRetrofitService::class.java)
    }
    @Singleton
    @Provides
    fun provideGroundRetrofitService(retrofit: Retrofit): GroundRemoteDataSource {

        // Whenever Dagger needs to provide an instance,
        // this code (the one inside the @Provides method) is run.
        return retrofit
            .create(GroundRemoteDataSource::class.java)
    }

    @Singleton
    @Provides
    fun getRetrofit(): Retrofit {
        val baseUrl = "http://beydgek.com/"
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