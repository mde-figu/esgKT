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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Forest
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.style.TextAlign
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
import com.ecotracker.viewmodel.FoodViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FoodImpactScreen(
    onNavigateBack: () -> Unit,
    viewModel: FoodViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            EcoTopBar(
                title = "\uD83C\uDF5D Impacto Alimentar",
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
            // Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = "\uD83C\uDF3F A produção de alimentos é responsável por cerca de 26% das emissões globais de gases de efeito estufa. Selecione os alimentos que você consumiu hoje:",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Food Selection
            SectionTitle(text = "Selecione os Alimentos")

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                viewModel.foodOptions.forEachIndexed { index, option ->
                    Surface(
                        modifier = Modifier.clickable { viewModel.addFood(index) },
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, Color.LightGray),
                        shadowElevation = 2.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = option.icon, fontSize = 28.sp)
                            Text(
                                text = option.name,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "${String.format("%.1f", option.emissionPerKg * option.defaultServing)} kg CO₂",
                                style = MaterialTheme.typography.labelSmall,
                                color = OrangePrimary
                            )
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Adicionar",
                                tint = GreenPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Selected Items
            if (uiState.selectedItems.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionTitle(text = "Sua Refeição")
                    OutlinedButton(
                        onClick = { viewModel.clearAll() },
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

                // Stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Total Emissões",
                        value = String.format("%.2f", uiState.totalEmissions),
                        unit = "kg CO₂",
                        icon = Icons.Default.Restaurant,
                        color = OrangePrimary,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                StatCard(
                    title = "Árvores para Compensar (por dia)",
                    value = String.format("%.0f", uiState.treesNeeded),
                    unit = "árvores",
                    icon = Icons.Default.Forest,
                    color = GreenPrimary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // List of selected foods with emission bars
                val maxEmission = uiState.selectedItems.maxOfOrNull { it.totalEmission } ?: 1.0
                uiState.selectedItems.forEachIndexed { index, item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = item.icon, fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                EmissionBar(
                                    label = item.name,
                                    value = item.totalEmission,
                                    maxValue = maxEmission,
                                    color = when {
                                        item.totalEmission > 3 -> OrangePrimary
                                        item.totalEmission > 1 -> BluePrimary
                                        else -> GreenPrimary
                                    }
                                )
                            }
                            IconButton(onClick = { viewModel.removeFood(index) }) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Remover",
                                    tint = Color.Red.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                    if (index < uiState.selectedItems.size - 1) {
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Comparison section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "\uD83D\uDCCA Comparação de Impacto",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val comparisons = listOf(
                        "1 kg de carne bovina = 27 kg CO₂ (equivale a dirigir 128 km)",
                        "1 kg de frango = 6.9 kg CO₂ (4x menos que carne bovina)",
                        "1 kg de vegetais = 2.0 kg CO₂ (13x menos que carne bovina)",
                        "1 kg de feijão = 0.9 kg CO₂ (30x menos que carne bovina)"
                    )
                    comparisons.forEach { comparison ->
                        Text(
                            text = "• $comparison",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(vertical = 2.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
