package com.example.smiley.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val okHttpClient = buildOkHttpClient()
    private const val BASE_URL = "http://10.0.2.2" // Emulator to localhost
//    private const val BASE_URL = "http://127.0.0.1" // Conected device to localhost

    fun getDbApiInstance(): DatabaseApi {
        val builder = Retrofit.Builder().baseUrl("$BASE_URL:3000/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return builder.create(DatabaseApi::class.java)
    }

    fun getMlApiInstance(): ClassificationApi {
        val builder = Retrofit.Builder().baseUrl("$BASE_URL:5000/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return builder.create(ClassificationApi::class.java)
    }

    private fun buildOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }
}