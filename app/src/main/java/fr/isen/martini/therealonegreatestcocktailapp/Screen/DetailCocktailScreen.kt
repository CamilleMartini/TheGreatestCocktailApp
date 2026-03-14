package fr.isen.martini.thegreatestcocktailapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun DetailCocktailScreen(drinkId: String? = null) {
    val context = LocalContext.current
    var cocktail by remember { mutableStateOf<CocktailDetail?>(null) }
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = drinkId) {
        try {
            val response = if (drinkId.isNullOrEmpty()) {
                NetworkManager.apiService.getRandomCocktail()
            } else {
                NetworkManager.apiService.getDrinkDetail(drinkId)
            }
            cocktail = response.drinks?.firstOrNull()

            cocktail?.let {
                isFavorite = FavoritesManager.getFavorites(context).contains(it.idDrink)
            }
        } catch (e: Exception) { }
    }

    cocktail?.let { data ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE8F5E9)) // Fond vert pâle
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = data.strDrinkThumb,
                contentDescription = data.strDrink,
                modifier = Modifier
                    .size(250.dp)
                    .clip(CircleShape)
                    .padding(top = 10.dp),
                contentScale = ContentScale.Crop
            )

            IconButton(
                onClick = {
                    FavoritesManager.toggleFavorite(context, data.idDrink)
                    isFavorite = !isFavorite
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favori",
                    tint = if (isFavorite) Color.Red else Color.DarkGray,
                    modifier = Modifier.size(32.dp)
                )
            }

            Text(
                text = data.strDrink,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20)
            )

            Text(
                text = data.strCategory,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Instructions",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF2E7D32)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = data.strInstructions,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Color(0xFF2E7D32))
    }
}
