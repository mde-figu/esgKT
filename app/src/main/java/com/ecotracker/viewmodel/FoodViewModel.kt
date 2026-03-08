package com.ecotracker.viewmodel

import androidx.lifecycle.ViewModel
import com.ecotracker.data.model.EmissionFactors
import com.ecotracker.data.model.FoodEmission
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class FoodUiState(
    val selectedItems: List<FoodEmission> = emptyList(),
    val totalEmissions: Double = 0.0,
    val treesNeeded: Double = 0.0
)

class FoodViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FoodUiState())
    val uiState: StateFlow<FoodUiState> = _uiState.asStateFlow()

    data class FoodOption(
        val name: String,
        val category: String,
        val icon: String,
        val emissionPerKg: Double,
        val defaultServing: Double // in kg
    )

    val foodOptions = listOf(
        FoodOption("Carne Bovina", "Proteína", "\uD83E\uDD69", EmissionFactors.BEEF, 0.2),
        FoodOption("Carne Suína", "Proteína", "\uD83E\uDD53", EmissionFactors.PORK, 0.2),
        FoodOption("Frango", "Proteína", "\uD83C\uDF57", EmissionFactors.CHICKEN, 0.2),
        FoodOption("Peixe", "Proteína", "\uD83D\uDC1F", EmissionFactors.FISH, 0.15),
        FoodOption("Ovos", "Proteína", "\uD83E\uDD5A", EmissionFactors.EGGS, 0.12),
        FoodOption("Queijo", "Laticínios", "\uD83E\uDDC0", EmissionFactors.CHEESE, 0.05),
        FoodOption("Leite (1L)", "Laticínios", "\uD83E\uDD5B", EmissionFactors.MILK, 1.0),
        FoodOption("Arroz", "Grãos", "\uD83C\uDF5A", EmissionFactors.RICE, 0.2),
        FoodOption("Pão", "Grãos", "\uD83C\uDF5E", EmissionFactors.BREAD, 0.1),
        FoodOption("Vegetais", "Vegetais", "\uD83E\uDD66", EmissionFactors.VEGETABLES, 0.3),
        FoodOption("Frutas", "Frutas", "\uD83C\uDF4E", EmissionFactors.FRUITS, 0.2),
        FoodOption("Feijão", "Leguminosas", "\uD83E\uDED8", EmissionFactors.BEANS, 0.2)
    )

    fun addFood(foodIndex: Int) {
        val option = foodOptions[foodIndex]
        val emission = FoodEmission(
            name = option.name,
            category = option.category,
            icon = option.icon,
            emissionPerKg = option.emissionPerKg,
            servingSize = option.defaultServing,
            totalEmission = option.emissionPerKg * option.defaultServing
        )

        val newItems = _uiState.value.selectedItems + emission
        val newTotal = newItems.sumOf { it.totalEmission }
        val trees = newTotal / EmissionFactors.TREE_DAILY_ABSORPTION_KG

        _uiState.value = FoodUiState(
            selectedItems = newItems,
            totalEmissions = newTotal,
            treesNeeded = trees
        )
    }

    fun removeFood(index: Int) {
        val newItems = _uiState.value.selectedItems.toMutableList().apply { removeAt(index) }
        val newTotal = newItems.sumOf { it.totalEmission }
        val trees = newTotal / EmissionFactors.TREE_DAILY_ABSORPTION_KG

        _uiState.value = FoodUiState(
            selectedItems = newItems,
            totalEmissions = newTotal,
            treesNeeded = trees
        )
    }

    fun clearAll() {
        _uiState.value = FoodUiState()
    }
}
