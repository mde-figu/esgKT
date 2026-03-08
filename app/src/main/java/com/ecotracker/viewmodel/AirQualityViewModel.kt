package com.ecotracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecotracker.data.model.AirQualityComponents
import com.ecotracker.data.model.AirQualityData
import com.ecotracker.data.model.AirQualityMain
import com.ecotracker.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class AirQualityUiState(
    val isLoading: Boolean = false,
    val airQualityData: AirQualityData? = null,
    val errorMessage: String? = null,
    val selectedCity: String = "São Paulo"
)

class AirQualityViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AirQualityUiState())
    val uiState: StateFlow<AirQualityUiState> = _uiState.asStateFlow()

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    data class CityOption(
        val name: String,
        val lat: Double,
        val lon: Double
    )

    val cities = listOf(
        CityOption("São Paulo", -23.5505, -46.6333),
        CityOption("Rio de Janeiro", -22.9068, -43.1729),
        CityOption("Brasília", -15.7975, -47.8919),
        CityOption("Salvador", -12.9714, -38.5124),
        CityOption("Belo Horizonte", -19.9167, -43.9345),
        CityOption("Curitiba", -25.4284, -49.2733),
        CityOption("Manaus", -3.1190, -60.0217),
        CityOption("Recife", -8.0476, -34.8770)
    )

    init {
        fetchAirQuality(0)
    }

    fun fetchAirQuality(cityIndex: Int) {
        val city = cities[cityIndex]
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null,
            selectedCity = city.name
        )

        viewModelScope.launch {
            try {
                val data = withContext(Dispatchers.IO) {
                    fetchAirQualityFromApi(city.lat, city.lon)
                }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    airQualityData = data,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Erro ao carregar dados: ${e.localizedMessage}"
                )
            }
        }
    }

    /**
     * Fetches air quality data using OkHttp directly and parses JSON manually.
     * This bypasses Retrofit and Gson entirely, avoiding all reflection-based
     * type resolution that R8 breaks in release builds.
     */
    private fun fetchAirQualityFromApi(lat: Double, lon: Double): AirQualityData {
        val url = "https://api.openweathermap.org/data/2.5/air_pollution" +
            "?lat=$lat&lon=$lon&appid=${RetrofitClient.API_KEY}"

        val request = Request.Builder().url(url).build()
        val response = httpClient.newCall(request).execute()

        if (!response.isSuccessful) {
            throw Exception("HTTP ${response.code}: ${response.message}")
        }

        val bodyString = response.body?.string()
            ?: throw Exception("Resposta vazia do servidor")

        val json = JSONObject(bodyString)
        val listArray = json.getJSONArray("list")
        val firstItem = listArray.getJSONObject(0)

        val mainObj = firstItem.getJSONObject("main")
        val main = AirQualityMain(aqi = mainObj.getInt("aqi"))

        val compObj = firstItem.getJSONObject("components")
        val components = AirQualityComponents(
            co = compObj.optDouble("co", 0.0),
            no = compObj.optDouble("no", 0.0),
            no2 = compObj.optDouble("no2", 0.0),
            o3 = compObj.optDouble("o3", 0.0),
            so2 = compObj.optDouble("so2", 0.0),
            pm25 = compObj.optDouble("pm2_5", 0.0),
            pm10 = compObj.optDouble("pm10", 0.0),
            nh3 = compObj.optDouble("nh3", 0.0)
        )

        val dt = firstItem.getLong("dt")

        return AirQualityData(main = main, components = components, dt = dt)
    }

    fun getAqiLabel(aqi: Int): String = when (aqi) {
        1 -> "Boa"
        2 -> "Razoável"
        3 -> "Moderada"
        4 -> "Ruim"
        5 -> "Muito Ruim"
        else -> "Desconhecido"
    }

    fun getAqiDescription(aqi: Int): String = when (aqi) {
        1 -> "A qualidade do ar é satisfatória e a poluição apresenta pouco ou nenhum risco."
        2 -> "A qualidade do ar é aceitável. Pode haver risco para pessoas sensíveis."
        3 -> "Membros de grupos sensíveis podem apresentar efeitos na saúde."
        4 -> "Todos podem começar a sentir efeitos na saúde."
        5 -> "Alerta de saúde: todos podem experimentar efeitos graves."
        else -> "Dados indisponíveis."
    }
}
