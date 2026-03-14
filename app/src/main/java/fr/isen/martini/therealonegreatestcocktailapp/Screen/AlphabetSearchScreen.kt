package fr.isen.martini.thegreatestcocktailapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
fun AlphabetSearchScreen(onDrinkClick: (String) -> Unit) {
    val alphabet = ('A'..'Z').map { it.toString() }
    var selectedLetter by remember { mutableStateOf("A") }
    var drinks by remember { mutableStateOf<List<DrinkBrief>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(selectedLetter) {
        try {
            isLoading = true
            val response = NetworkManager.apiService.getDrinksByFirstLetter(selectedLetter)
            drinks = response.drinks ?: emptyList()
        } catch (e: Exception) {
            drinks = emptyList()
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F5E9))
            .padding(16.dp)
    ) {
        Text(
            text = "Exploration par Alphabet",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1B5E20)
        )

        LazyRow(
            modifier = Modifier.padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(alphabet) { letter ->
                FilterChip(
                    selected = selectedLetter == letter,
                    onClick = { selectedLetter = letter },
                    label = { Text(letter) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF2E7D32),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF2E7D32))
            }
        } else if (drinks.isEmpty()) {
            Text("Aucun cocktail trouvé pour la lettre $selectedLetter", modifier = Modifier.padding(top = 20.dp))
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(drinks) { drink ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onDrinkClick(drink.idDrink) },
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