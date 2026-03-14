package fr.isen.martini.thegreatestcocktailapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
fun IngredientSearchScreen(onDrinkClick: (String) -> Unit) {
    var ingredientQuery by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<DrinkBrief>>(emptyList()) }
    var isSearching by remember { mutableStateOf(false) }

    LaunchedEffect(ingredientQuery) {
        if (ingredientQuery.length > 2) {
            try {
                isSearching = true
                val response = NetworkManager.apiService.filterByIngredient(ingredientQuery)
                results = response.drinks ?: emptyList()
            } catch (e: Exception) {
                results = emptyList()
            } finally {
                isSearching = false
            }
        } else {
            results = emptyList()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F5E9))
            .padding(16.dp)
    ) {
        Text(
            text = "Filtrer par Ingrédient",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1B5E20)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = ingredientQuery,
            onValueChange = { ingredientQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Ex: Gin, Vodka, Rum...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isSearching) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF2E7D32))
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(results) { drink ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onDrinkClick(drink.idDrink) },
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        ListItem(
                            headlineContent = { Text(drink.strDrink, fontWeight = FontWeight.Bold) },
                            leadingContent = {
                                AsyncImage(
                                    model = drink.strDrinkThumb,
                                    contentDescription = null,
                                    modifier = Modifier.size(50.dp).clip(CircleShape)
                                )
                            },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                        )
                    }
                }
            }
        }
    }
}