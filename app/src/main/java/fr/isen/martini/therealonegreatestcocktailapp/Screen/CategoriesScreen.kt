package fr.isen.martini.thegreatestcocktailapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CategoriesScreen(
    onCategoryClick: (String) -> Unit,
    onAlphabetClick: () -> Unit,
    onFiltersClick: () -> Unit,
    onIngredientInfoClick: () -> Unit,
    onRandomClick: () -> Unit
) {
    val categories = listOf("Beer", "Cocktail", "Cocoa", "Coffee", "Shot", "Soft Drink", "Other/Unknown")

    val lightGreenBackground = Color(0xFFE8F5E9)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(lightGreenBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "The Soju Cocktail Bar",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1B5E20),
            modifier = Modifier.padding(top = 10.dp, bottom = 5.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.soju),
            contentDescription = "Soju Logo",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Outils Rapides",
            style = MaterialTheme.typography.labelLarge,
            color = Color(0xFF2E7D32),
            modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            QuickActionButton(Icons.Default.SortByAlpha, "A-Z", onAlphabetClick)
            QuickActionButton(Icons.Default.Tune, "Filtres", onFiltersClick)
            QuickActionButton(Icons.Default.MenuBook, "Dico", onIngredientInfoClick)
            QuickActionButton(Icons.Default.Casino, "Random", onRandomClick)
        }

        Divider(color = Color(0xFFC8E6C9), thickness = 1.dp, modifier = Modifier.padding(bottom = 16.dp))

        Text(
            text = "Catégories",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 12.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { category ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCategoryClick(category) },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.LocalBar, contentDescription = null, tint = Color(0xFF2E7D32))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = category,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun QuickActionButton(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        FilledIconButton(
            onClick = onClick,
            colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color(0xFF2E7D32))
        ) {
            Icon(icon, contentDescription = label, tint = Color.White)
        }
        Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF2E7D32))
    }
}