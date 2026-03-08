package com.ecotracker.data.model

data class TransportEmission(
    val type: String,
    val icon: String,
    val distanceKm: Double,
    val emissionFactor: Double, // kg CO2 per km
    val totalEmission: Double   // kg CO2
)

data class FoodEmission(
    val name: String,
    val category: String,
    val icon: String,
    val emissionPerKg: Double, // kg CO2 per kg of food
    val servingSize: Double,   // typical serving in kg
    val totalEmission: Double  // kg CO2 for the serving
)

data class DailySummary(
    val transportEmissions: Double,
    val foodEmissions: Double,
    val totalEmissions: Double,
    val treesNeeded: Double // trees needed to offset
)

object EmissionFactors {
    // Transport emission factors in kg CO2 per km
    const val CAR_GASOLINE = 0.21
    const val CAR_DIESEL = 0.17
    const val CAR_ELECTRIC = 0.05
    const val BUS = 0.089
    const val TRAIN = 0.041
    const val AIRPLANE = 0.255
    const val MOTORCYCLE = 0.103
    const val BICYCLE = 0.0

    // Food emission factors in kg CO2 per kg of food
    const val BEEF = 27.0
    const val PORK = 12.1
    const val CHICKEN = 6.9
    const val FISH = 6.1
    const val EGGS = 4.8
    const val CHEESE = 13.5
    const val MILK = 3.2
    const val RICE = 2.7
    const val BREAD = 0.8
    const val VEGETABLES = 2.0
    const val FRUITS = 1.1
    const val BEANS = 0.9

    // Average tree CO2 absorption: ~22 kg CO2 per year = ~0.06 kg per day
    const val TREE_DAILY_ABSORPTION_KG = 0.06
}
