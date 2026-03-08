package com.ecotracker.data.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

data class AirQualityResponse(
    val coord: List<Double>? = null,
    val list: List<AirQualityData>
)

data class AirQualityData(
    val main: AirQualityMain,
    val components: AirQualityComponents,
    val dt: Long
)

data class AirQualityMain(
    val aqi: Int
)

data class AirQualityComponents(
    val co: Double,
    val no: Double,
    val no2: Double,
    val o3: Double,
    val so2: Double,
    val pm25: Double,
    val pm10: Double,
    val nh3: Double
)

/**
 * Custom deserializer that manually parses the Air Quality API JSON response.
 * This avoids Gson's reflection-based generic type resolution which breaks under R8.
 */
class AirQualityResponseDeserializer : JsonDeserializer<AirQualityResponse> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): AirQualityResponse {
        val jsonObject = json.asJsonObject

        // coord is a JSON object {"lon":..,"lat":..}, not an array - extract as list
        val coord = try {
            if (jsonObject.has("coord")) {
                val coordObj = jsonObject.getAsJsonObject("coord")
                listOf(coordObj.get("lat").asDouble, coordObj.get("lon").asDouble)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }

        val listArray = jsonObject.getAsJsonArray("list")
        val dataList = listArray.map { element ->
            val dataObj = element.asJsonObject

            val mainObj = dataObj.getAsJsonObject("main")
            val main = AirQualityMain(aqi = mainObj.get("aqi").asInt)

            val compObj = dataObj.getAsJsonObject("components")
            val components = AirQualityComponents(
                co = compObj.get("co")?.asDouble ?: 0.0,
                no = compObj.get("no")?.asDouble ?: 0.0,
                no2 = compObj.get("no2")?.asDouble ?: 0.0,
                o3 = compObj.get("o3")?.asDouble ?: 0.0,
                so2 = compObj.get("so2")?.asDouble ?: 0.0,
                pm25 = compObj.get("pm2_5")?.asDouble ?: 0.0,
                pm10 = compObj.get("pm10")?.asDouble ?: 0.0,
                nh3 = compObj.get("nh3")?.asDouble ?: 0.0
            )

            val dt = dataObj.get("dt").asLong

            AirQualityData(main = main, components = components, dt = dt)
        }

        return AirQualityResponse(coord = coord, list = dataList)
    }
}
