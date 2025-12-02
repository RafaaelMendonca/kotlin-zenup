package org.example.network

import org.example.api.ZenUpApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://127.0.0.1:8000/"

    // variável mutável para armazenar token
    var authToken: String? = null

    private val client by lazy {
        provideOkHttpClient { authToken }
    }

    val api: ZenUpApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ZenUpApi::class.java)
    }
}