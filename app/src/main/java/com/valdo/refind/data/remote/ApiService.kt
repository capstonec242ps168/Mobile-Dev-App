package com.valdo.refind.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.Part
import okhttp3.MultipartBody
import retrofit2.http.Multipart

interface ApiService {

    @GET("/trashes")
    fun getTrashes(): Call<TrashesResponse>

    @GET("/crafts/{label}")
    fun getCraftsByLabel(@Path("label") label: String): Call<CraftsResponse>

    @GET("/craft/{id}")
    fun getCraftById(@Path("id") id: String): Call<CraftResponse>

    @Multipart
    @POST("/predict")
    fun postPrediction(
        @Part image: MultipartBody.Part
    ): Call<PredictResponse>

    @GET("/news")
    fun getNews(): Call<NewsResponse>

    @GET("/news/{id}")
    fun getNewsById(@Path("id") id: String): Call<NewsItem>
}
