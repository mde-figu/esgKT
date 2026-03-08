package com.ecotracker.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ecotracker.ui.components.EcoTopBar
import com.ecotracker.ui.components.SectionTitle
import com.ecotracker.ui.theme.BluePrimary
import com.ecotracker.ui.theme.GreenPrimary
import com.ecotracker.ui.theme.OrangePrimary
import com.ecotracker.viewmodel.AirQualityViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AirQualityScreen(
    onNavigateBack: () -> Unit,
    viewModel: AirQualityViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedCityIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            EcoTopBar(
                title = "\u2601\uFE0F Qualidade do Ar",
                onNavigateBack = onNavigateBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // API Info
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Cloud,
                        contentDescription = null,
                        tint = BluePrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Dados em tempo real fornecidos pela API OpenWeatherMap Air Pollution",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // City Selection
            SectionTitle(text = "Selecione a Cidade")

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                viewModel.cities.forEachIndexed { index, city ->
                    Surface(
                        modifier = Modifier.clickable {
                            selectedCityIndex = index
                            viewModel.fetchAirQuality(index)
                        },
                        shape = RoundedCornerShape(20.dp),
                        color = if (city.name == uiState.selectedCity)
                            BluePrimary else Color.White,
                        border = BorderStroke(
                            1.dp,
                            if (city.name == uiState.selectedCity)
                                BluePrimary else Color.LightGray
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationCity,
                                contentDescription = null,
                                tint = if (city.name == uiState.selectedCity)
                                    Color.White else BluePrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = city.name,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (city.name == uiState.selectedCity)
                                    Color.White else Color.DarkGray,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Loading State
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = GreenPrimary)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Carregando dados de ${uiState.selectedCity}...",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Error State
            uiState.errorMessage?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "\u26A0\uFE0F",
                            fontSize = 32.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = OrangePrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Nota: Para utilizar a API, obtenha sua chave gratuita em openweathermap.org/api",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        IconButton(onClick = { viewModel.fetchAirQuality(selectedCityIndex) }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Tentar novamente",
                                tint = BluePrimary
                            )
                        }
                    }
                }
            }

            // Data Display
            uiState.airQualityData?.let { data ->
                // AQI Display
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = uiState.selectedCity,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // AQI Circle
                        val aqiColor = when (data.main.aqi) {
                            1 -> GreenPrimary
                            2 -> Color(0xFF8BC34A)
                            3 -> Color(0xFFFFC107)
                            4 -> OrangePrimary
                            5 -> Color.Red
                            else -> Color.Gray
                        }

                        Surface(
                            modifier = Modifier.size(120.dp),
                            shape = CircleShape,
                            color = aqiColor.copy(alpha = 0.15f),
                            border = BorderStroke(4.dp, aqiColor)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "${data.main.aqi}",
                                    fontSize = 40.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = aqiColor
                                )
                                Text(
                                    text = "AQI",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = aqiColor
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = viewModel.getAqiLabel(data.main.aqi),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = aqiColor
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = viewModel.getAqiDescription(data.main.aqi),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Pollutant Details
                SectionTitle(text = "Detalhes dos Poluentes")

                PollutantRow(label = "PM2.5", value = data.components.pm25)
                PollutantRow(label = "PM10", value = data.components.pm10)
                PollutantRow(label = "O3 (Ozonio)", value = data.components.o3)
                PollutantRow(label = "NO2 (Dioxido de Nitrogenio)", value = data.components.no2)
                PollutantRow(label = "SO2 (Dioxido de Enxofre)", value = data.components.so2)
                PollutantRow(label = "CO (Monoxido de Carbono)", value = data.components.co)
                PollutantRow(label = "NH3 (Amonia)", value = data.components.nh3)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // AQI Scale Legend
            SectionTitle(text = "Escala AQI")

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    val aqiLevels = listOf(
                        Triple("1 - Boa", GreenPrimary, "Qualidade do ar satisfatória"),
                        Triple("2 - Razoável", Color(0xFF8BC34A), "Aceitável para a maioria"),
                        Triple("3 - Moderada", Color(0xFFFFC107), "Risco para grupos sensíveis"),
                        Triple("4 - Ruim", OrangePrimary, "Efeitos na saúde possíveis"),
                        Triple("5 - Muito Ruim", Color.Red, "Alerta de saúde geral")
                    )

                    aqiLevels.forEach { (level, color, description) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier.size(12.dp),
                                shape = CircleShape,
                                color = color
                            ) {}
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = level,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.width(120.dp)
                            )
                            Text(
                                text = description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // API Info
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "\uD83D\uDD17 Serviço Utilizado",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "OpenWeatherMap Air Pollution API",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "https://api.openweathermap.org/data/2.5/air_pollution",
                        style = MaterialTheme.typography.bodySmall,
                        color = BluePrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PollutantRow(label: String, value: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Air,
                    contentDescription = null,
                    tint = BluePrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = String.format("%.1f", value) + " µg/m³",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = BluePrimary,
                modifier = Modifier.padding(start = 28.dp)
            )
        }
    }
}
