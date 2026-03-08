package com.ecotracker.data.remote

/**
 * Holds the API key constant for OpenWeatherMap.
 * Note: Retrofit/Gson setup was removed because it caused ParameterizedType
 * crashes under R8 minification even when unused. The air quality API calls
 * now use OkHttp directly (see AirQualityViewModel).
 */
object RetrofitClient {
    // OpenWeatherMap Air Pollution API key
    // Get your own free key at https://openweathermap.org/api
    const val API_KEY = "234dbdb4ffb4189d33de80cfb2b6f424"
}
