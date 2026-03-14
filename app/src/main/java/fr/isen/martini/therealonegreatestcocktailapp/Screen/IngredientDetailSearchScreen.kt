package fr.isen.martini.thegreatestcocktailapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun IngredientInfoScreen() {
    var query by remember { mutableStateOf("") }
    var ingredient by remember { mutableStateOf<IngredientDetail?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(query) {
        if (query.length > 2) {
            try {
                isLoading = true
                val response = NetworkManager.apiService.getIngredientDetails(query)
                ingredient = response.ingredients?.firstOrNull()
            } catch (e: Exception) {
                ingredient = null
            } finally {
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F5E9))
            .padding(16.dp)
    ) {
        Text(
            text = "Dico Ingrédients",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1B5E20)
        )

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            placeholder = { Text("Ex: Vodka, Gin, Sugar...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF2E7D32))
            }
        }

        ingredient?.let { info ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(info.strIngredient, style = MaterialTheme.typography.headlineMedium, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                    Text("Type: ${info.strType ?: "Inconnu"}", style = MaterialTheme.typography.bodyLarge)
                    Text("Alcool: ${info.strAlcohol ?: "Non"}")
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(info.strDescription ?: "Aucune description disponible.")
                }
            }
        }
    }
}