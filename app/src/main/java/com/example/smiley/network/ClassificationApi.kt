package com.example.smiley.network

import com.example.smiley.models.ClassificationResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ClassificationApi {
    @POST("api/classify")
    @Multipart
    fun classify(@Part image: MultipartBody.Part): Call<ClassificationResponse>
}