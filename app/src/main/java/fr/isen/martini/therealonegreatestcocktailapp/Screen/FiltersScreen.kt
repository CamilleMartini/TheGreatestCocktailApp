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
fun FiltersScreen(onDrinkClick: (String) -> Unit) {
    var selectedFilterType by remember { mutableStateOf("Alcoholic") }
    var filterValue by remember { mutableStateOf("Alcoholic") }
    var drinks by remember { mutableStateOf<List<DrinkBrief>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(selectedFilterType, filterValue) {
        try {
            isLoading = true
            val response = when (selectedFilterType) {
                "Alcoholic" -> NetworkManager.apiService.filterByAlcoholic(filterValue)
                "Category" -> NetworkManager.apiService.filterByCategory(filterValue)
                else -> NetworkManager.apiService.filterByGlass(filterValue)
            }
            drinks = response.drinks ?: emptyList()
        } catch (e: Exception) { drinks = emptyList() }
        finally { isLoading = false }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFE8F5E9)).padding(16.dp)) {
        Text("Filtres Avancés", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))

        Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            FilterChip(selected = selectedFilterType == "Alcoholic", onClick = { selectedFilterType = "Alcoholic"; filterValue = "Alcoholic" }, label = { Text("Alcool") })
            FilterChip(selected = selectedFilterType == "Category", onClick = { selectedFilterType = "Category"; filterValue = "Cocktail" }, label = { Text("Catégorie") })
            FilterChip(selected = selectedFilterType == "Glass", onClick = { selectedFilterType = "Glass"; filterValue = "Cocktail_glass" }, label = { Text("Verre") })
        }

        val options = when(selectedFilterType) {
            "Alcoholic" -> listOf("Alcoholic", "Non_Alcoholic")
            "Category" -> listOf("Ordinary_Drink", "Cocktail", "Shake", "Cocoa")
            else -> listOf("Cocktail_glass", "Champagne_flute", "Shot_glass")
        }

        ScrollableTabRow(selectedTabIndex = options.indexOf(filterValue).coerceAtLeast(0), edgePadding = 0.dp, containerColor = Color.Transparent) {
            options.forEach { option ->
                Tab(selected = filterValue == option, onClick = { filterValue = option }, text = { Text(option.replace("_", " ")) })
            }
        }

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
                items(drinks) { drink ->
                    Card(modifier = Modifier.fillMaxWidth().clickable { onDrinkClick(drink.idDrink) }, colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        ListItem(
                            headlineContent = { Text(drink.strDrink, fontWeight = FontWeight.Bold) },
                            leadingContent = { AsyncImage(model = drink.strDrinkThumb, contentDescription = null, modifier = Modifier.size(50.dp).clip(CircleShape)) }
                        )
                    }
                }
            }
        }
    }
}