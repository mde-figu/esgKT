package com.ecotracker.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ecotracker.ui.components.EcoTopBar
import com.ecotracker.ui.components.EmissionBar
import com.ecotracker.ui.components.SectionTitle
import com.ecotracker.ui.components.StatCard
import com.ecotracker.ui.theme.BluePrimary
import com.ecotracker.ui.theme.GreenPrimary
import com.ecotracker.ui.theme.OrangePrimary
import com.ecotracker.viewmodel.TransportViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TransportScreen(
    onNavigateBack: () -> Unit,
    viewModel: TransportViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            EcoTopBar(
                title = "\uD83D\uDE97 Emissões de Transporte",
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
            // Transport Type Selection
            SectionTitle(text = "Tipo de Transporte")

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                viewModel.transportOptions.forEachIndexed { index, option ->
                    Surface(
                        modifier = Modifier.clickable { viewModel.selectTransport(index) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (index == uiState.selectedTransportIndex)
                            GreenPrimary else Color.White,
                        border = BorderStroke(
                            1.dp,
                            if (index == uiState.selectedTransportIndex)
                                GreenPrimary else Color.LightGray
                        ),
                        shadowElevation = 2.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = option.icon,
                                fontSize = 24.sp
                            )
                            Text(
                                text = option.name,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (index == uiState.selectedTransportIndex)
                                    Color.White else Color.DarkGray,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Emission factor info
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                val selectedOption = viewModel.transportOptions[uiState.selectedTransportIndex]
                Text(
                    text = "Fator de emissão: ${selectedOption.factor} kg CO₂/km",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Distance Input
            SectionTitle(text = "Distância")

            OutlinedTextField(
                value = uiState.distanceKm,
                onValueChange = { viewModel.updateDistance(it) },
                label = { Text("Distância (km)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = GreenPrimary
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Calculate Button
            Button(
                onClick = { viewModel.calculateEmission() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = uiState.distanceKm.toDoubleOrNull() != null && uiState.distanceKm.toDoubleOrNull()!! > 0
            ) {
                Icon(
                    imageVector = Icons.Default.Calculate,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Calcular Emissão", fontWeight = FontWeight.Bold)
            }

            // Result
            uiState.result?.let { result ->
                Spacer(modifier = Modifier.height(16.dp))

                StatCard(
                    title = "Última Emissão Calculada",
                    value = String.format("%.2f", result.totalEmission),
                    unit = "kg CO₂",
                    icon = Icons.Default.Calculate,
                    color = OrangePrimary
                )
            }

            // History
            if (uiState.history.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionTitle(text = "Histórico")
                    OutlinedButton(
                        onClick = { viewModel.clearHistory() },
                        border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.5f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Limpar",
                            tint = Color.Red.copy(alpha = 0.7f),
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text("Limpar", color = Color.Red.copy(alpha = 0.7f))
                    }
                }

                // Total emissions stat
                StatCard(
                    title = "Total de Emissões",
                    value = String.format("%.2f", uiState.totalEmissions),
                    unit = "kg CO₂",
                    icon = Icons.Default.DirectionsCar,
                    color = BluePrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                val maxEmission = uiState.history.maxOfOrNull { it.totalEmission } ?: 1.0
                uiState.history.forEachIndexed { index, emission ->
                    EmissionBar(
                        label = "${emission.icon} ${emission.type} - ${emission.distanceKm} km",
                        value = emission.totalEmission,
                        maxValue = maxEmission,
                        color = when {
                            emission.totalEmission > 5 -> OrangePrimary
                            emission.totalEmission > 1 -> BluePrimary
                            else -> GreenPrimary
                        }
                    )
                    if (index < uiState.history.size - 1) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tips section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "\uD83D\uDCA1 Dica",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Usar transporte público pode reduzir suas emissões em até 65% comparado ao carro individual. Considere também caronas compartilhadas!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
