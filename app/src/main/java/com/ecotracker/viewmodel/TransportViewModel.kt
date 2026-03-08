package com.ecotracker.viewmodel

import androidx.lifecycle.ViewModel
import com.ecotracker.data.model.EmissionFactors
import com.ecotracker.data.model.TransportEmission
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class TransportUiState(
    val selectedTransportIndex: Int = 0,
    val distanceKm: String = "",
    val result: TransportEmission? = null,
    val history: List<TransportEmission> = emptyList(),
    val totalEmissions: Double = 0.0
)

class TransportViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TransportUiState())
    val uiState: StateFlow<TransportUiState> = _uiState.asStateFlow()

    data class TransportOption(
        val name: String,
        val icon: String,
        val factor: Double
    )

    val transportOptions = listOf(
        TransportOption("Carro (Gasolina)", "\uD83D\uDE97", EmissionFactors.CAR_GASOLINE),
        TransportOption("Carro (Diesel)", "\uD83D\uDE99", EmissionFactors.CAR_DIESEL),
        TransportOption("Carro (Elétrico)", "\u26A1", EmissionFactors.CAR_ELECTRIC),
        TransportOption("Ônibus", "\uD83D\uDE8C", EmissionFactors.BUS),
        TransportOption("Trem", "\uD83D\uDE86", EmissionFactors.TRAIN),
        TransportOption("Avião", "\u2708\uFE0F", EmissionFactors.AIRPLANE),
        TransportOption("Moto", "\uD83C\uDFCD\uFE0F", EmissionFactors.MOTORCYCLE),
        TransportOption("Bicicleta", "\uD83D\uDEB2", EmissionFactors.BICYCLE)
    )

    fun selectTransport(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTransportIndex = index)
    }

    fun updateDistance(distance: String) {
        _uiState.value = _uiState.value.copy(distanceKm = distance)
    }

    fun calculateEmission() {
        val distance = _uiState.value.distanceKm.toDoubleOrNull() ?: return
        val option = transportOptions[_uiState.value.selectedTransportIndex]
        val total = distance * option.factor

        val emission = TransportEmission(
            type = option.name,
            icon = option.icon,
            distanceKm = distance,
            emissionFactor = option.factor,
            totalEmission = total
        )

        val newHistory = _uiState.value.history + emission
        val newTotal = newHistory.sumOf { it.totalEmission }

        _uiState.value = _uiState.value.copy(
            result = emission,
            history = newHistory,
            totalEmissions = newTotal,
            distanceKm = ""
        )
    }

    fun clearHistory() {
        _uiState.value = TransportUiState()
    }
}
