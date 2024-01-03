package ru.openunity.hunterhint.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.openunity.hunterhint.network.ApiService
import java.util.concurrent.TimeUnit


/**
 * Dependency Injection container at the application level.
 */
interface AppContainer {
    val repository: GroundsRepository
}

/**
 * Implementation for the Dependency Injection container at the application level.
 *
 * Variables are initialized lazily and the same instance is shared across the whole app.
 */
class DefaultAppContainer : AppContainer {
    private val baseUrl = "http://openunity.ru/"
    private val timeoutInSecond:Long = 10

    /**
     * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
     */
    private val retrofit: Retrofit = buildRetrofit()

    private fun buildRetrofit():Retrofit {
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

    /**
     * Retrofit service object for creating api calls
     */
    private val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    override val repository: GroundsRepository by lazy {
        NetworkGroundsRepository(retrofitService)
    }
}
