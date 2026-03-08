package com.ecotracker.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ecotracker.ui.components.EcoTopBar
import com.ecotracker.ui.components.InfoCard
import com.ecotracker.ui.components.SectionTitle
import com.ecotracker.ui.theme.BluePrimary
import com.ecotracker.ui.theme.GreenPrimary
import com.ecotracker.ui.theme.OrangePrimary

@Composable
fun EcoTipsScreen(onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            EcoTopBar(
                title = "\uD83C\uDF31 Dicas Sustentáveis",
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
            // Intro
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = GreenPrimary)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Pequenas Ações, Grande Impacto",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Cada escolha sustentável contribui para um futuro melhor. Conheça práticas alinhadas aos pilares ESG que você pode adotar no dia a dia.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Environmental Tips (E)
            SectionTitle(text = "\uD83C\uDF0D Ambiental (E)")

            InfoCard(
                title = "Reduza o Consumo de Energia",
                description = "Troque lâmpadas por LED, desligue aparelhos da tomada e use eletrodomésticos com selo Procel A. Isso pode reduzir sua conta de energia em até 30%.",
                icon = Icons.Default.Bolt,
                color = OrangePrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            InfoCard(
                title = "Transporte Sustentável",
                description = "Prefira transporte público, bicicleta ou carona. Um ônibus cheio pode substituir até 40 carros. Trabalhar de casa 2 dias por semana reduz suas emissões em 40%.",
                icon = Icons.Default.DirectionsBus,
                color = BluePrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            InfoCard(
                title = "Economize Água",
                description = "Banhos de 5 minutos economizam até 90 litros. Reutilize água da chuva para plantas e limpeza. O Brasil tem 12% da água doce mundial, mas desperdiça 40%.",
                icon = Icons.Default.WaterDrop,
                color = BluePrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            InfoCard(
                title = "Alimentação Consciente",
                description = "Reduza o consumo de carne vermelha (1 dia sem carne por semana = 340 kg CO₂/ano a menos). Prefira alimentos locais e da estação.",
                icon = Icons.Default.Restaurant,
                color = GreenPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            InfoCard(
                title = "Recicle e Reutilize",
                description = "O Brasil recicla apenas 4% dos resíduos. Separe seu lixo, composte orgânicos e evite descartáveis. Uma garrafa PET demora 400 anos para se decompor.",
                icon = Icons.Default.Recycling,
                color = GreenPrimary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Social Tips (S)
            SectionTitle(text = "\uD83E\uDD1D Social (S)")

            InfoCard(
                title = "Consumo Consciente",
                description = "Prefira empresas com práticas sustentáveis e comércio justo. Antes de comprar, pergunte: 'Eu realmente preciso disso?'. Reduza, reutilize, recicle.",
                icon = Icons.Default.ShoppingBag,
                color = OrangePrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            InfoCard(
                title = "Engajamento Comunitário",
                description = "Participe de mutirões de limpeza, plantio de árvores e ações sociais na sua comunidade. Voluntariado ambiental fortalece laços sociais e o meio ambiente.",
                icon = Icons.Default.Groups,
                color = BluePrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            InfoCard(
                title = "Evite Desperdício Alimentar",
                description = "30% dos alimentos produzidos no Brasil são desperdiçados. Planeje suas compras, armazene corretamente e doe excedentes para bancos de alimentos.",
                icon = Icons.Default.LocalDrink,
                color = GreenPrimary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Governance Tips (G)
            SectionTitle(text = "\uD83C\uDFDB\uFE0F Governança (G)")

            InfoCard(
                title = "Fiscalize e Participe",
                description = "Acompanhe as políticas ambientais do seu município. Participe de audiências públicas e cobre transparência dos órgãos responsáveis.",
                icon = Icons.Default.AccountBalance,
                color = OrangePrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            InfoCard(
                title = "Apoie Empresas ESG",
                description = "Invista em empresas comprometidas com práticas ESG. No Brasil, o ISE B3 lista empresas com alto desempenho em sustentabilidade corporativa.",
                icon = Icons.Default.Eco,
                color = GreenPrimary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Did you know section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "\uD83D\uDCA1 Você Sabia?",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = GreenPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val facts = listOf(
                        "O Brasil é o 4º maior emissor de gases de efeito estufa do mundo.",
                        "A Amazônia absorve cerca de 2 bilhões de toneladas de CO₂ por ano.",
                        "Até 2030, o Brasil se comprometeu a reduzir 50% das emissões (Acordo de Paris).",
                        "O desmatamento é responsável por 46% das emissões brasileiras.",
                        "Energias renováveis já representam 83% da matriz elétrica brasileira.",
                        "Uma única árvore pode absorver ~22 kg de CO₂ por ano."
                    )

                    facts.forEach { fact ->
                        Text(
                            text = "• $fact",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 3.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SDG Alignment
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "\uD83C\uDF10 ODS Alinhados",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = BluePrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val sdgs = listOf(
                        "ODS 7 - Energia Limpa e Acessível",
                        "ODS 11 - Cidades e Comunidades Sustentáveis",
                        "ODS 12 - Consumo e Produção Responsáveis",
                        "ODS 13 - Ação Contra a Mudança Global do Clima",
                        "ODS 15 - Vida Terrestre"
                    )

                    sdgs.forEach { sdg ->
                        Text(
                            text = "• $sdg",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
