package com.ecotracker.data.remote

import com.ecotracker.data.model.AirQualityResponse
import com.ecotracker.data.model.AirQualityResponseDeserializer
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://api.openweathermap.org/"

    // OpenWeatherMap Air Pollution API key
    // Get your own free key at https://openweathermap.org/api
    const val API_KEY = "234dbdb4ffb4189d33de80cfb2b6f424"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = GsonBuilder()
        .registerTypeAdapter(AirQualityResponse::class.java, AirQualityResponseDeserializer())
        .create()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val airQualityApi: AirQualityApi = retrofit.create(AirQualityApi::class.java)
}
