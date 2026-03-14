package fr.isen.martini.thegreatestcocktailapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun DrinksListScreen(categoryName: String, onDrinkClick: (String) -> Unit) {
    var drinks by remember { mutableStateOf<List<DrinkBrief>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    val realCategoryName = categoryName.replace("_SLASH_", "/")

    LaunchedEffect(categoryName) {
        try {
            isLoading = true
            val apiCategory = when (categoryName) {
                "Coffee" -> "Coffee / Tea"
                "Other_Unknown" -> "Other / Unknown"
                "Other/Unknown" -> "Other / Unknown"
                else -> categoryName
            }
            val response = NetworkManager.apiService.getDrinksByCategory(apiCategory)
            drinks = response.drinks ?: emptyList()
        } catch (e: Exception) {
            // Gérer l'erreur
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F5E9)) // Fond vert pâle
            .padding(16.dp)
    ) {
        Text(
            text = "Boissons : $realCategoryName",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E7D32) // Vert foncé
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF2E7D32))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(drinks) { drink ->
                    ListItem(
                        headlineContent = {
                            Text(drink.strDrink, fontWeight = FontWeight.Medium)
                        },
                        leadingContent = {
                            AsyncImage(
                                model = drink.strDrinkThumb,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                            )
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent
                        ),
                        modifier = Modifier.clickable { onDrinkClick(drink.idDrink) }
                    )
                    HorizontalDivider(color = Color(0xFFC8E6C9))
                }
            }
        }
    }
}