package com.ecotracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecotracker.data.model.AirQualityData
import com.ecotracker.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AirQualityUiState(
    val isLoading: Boolean = false,
    val airQualityData: AirQualityData? = null,
    val errorMessage: String? = null,
    val selectedCity: String = "São Paulo"
)

class AirQualityViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AirQualityUiState())
    val uiState: StateFlow<AirQualityUiState> = _uiState.asStateFlow()

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
                val response = RetrofitClient.airQualityApi.getAirQuality(
                    lat = city.lat,
                    lon = city.lon,
                    apiKey = RetrofitClient.API_KEY
                )
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    airQualityData = response.list.firstOrNull(),
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
