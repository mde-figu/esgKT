package com.ecotracker.data.model

import com.google.gson.annotations.SerializedName

data class AirQualityResponse(
    @SerializedName("coord") val coord: List<Double>? = null,
    @SerializedName("list") val list: List<AirQualityData>
)

data class AirQualityData(
    @SerializedName("main") val main: AirQualityMain,
    @SerializedName("components") val components: AirQualityComponents,
    @SerializedName("dt") val dt: Long
)

data class AirQualityMain(
    @SerializedName("aqi") val aqi: Int
)

data class AirQualityComponents(
    @SerializedName("co") val co: Double,
    @SerializedName("no") val no: Double,
    @SerializedName("no2") val no2: Double,
    @SerializedName("o3") val o3: Double,
    @SerializedName("so2") val so2: Double,
    @SerializedName("pm2_5") val pm25: Double,
    @SerializedName("pm10") val pm10: Double,
    @SerializedName("nh3") val nh3: Double
)
