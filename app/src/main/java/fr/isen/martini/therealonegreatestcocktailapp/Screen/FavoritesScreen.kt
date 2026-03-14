package fr.isen.martini.thegreatestcocktailapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun FavoritesScreen(onDrinkClick: (String) -> Unit) {
    val context = LocalContext.current
    var favoriteDrinks by remember { mutableStateOf<List<DrinkBrief>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val ids = FavoritesManager.getFavorites(context)
        val list = mutableListOf<DrinkBrief>()

        ids.forEach { id ->
            try {
                val response = NetworkManager.apiService.getDrinkDetail(id)
                response.drinks?.firstOrNull()?.let {
                    list.add(DrinkBrief(it.strDrink, it.strDrinkThumb, it.idDrink))
                }
            } catch (e: Exception) {
            }
        }
        favoriteDrinks = list
        isLoading = false
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Mes Favoris",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (favoriteDrinks.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Vous n'avez pas encore de favoris.")
            }
        } else {
            LazyColumn {
                items(favoriteDrinks) { drink ->
                    ListItem(
                        headlineContent = { Text(drink.strDrink) },
                        leadingContent = {
                            AsyncImage(
                                model = drink.strDrinkThumb,
                                contentDescription = null,
                                modifier = Modifier.size(56.dp)
                            )
                        },
                        modifier = Modifier.clickable { onDrinkClick(drink.idDrink) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}