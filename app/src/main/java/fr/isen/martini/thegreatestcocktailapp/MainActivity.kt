package fr.isen.martini.thegreatestcocktailapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fr.isen.martini.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheGreatestCocktailAppTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                val bottomNavItems = listOf(
                    Triple("categories", Icons.Default.Home, "Accueil"),
                    Triple("search", Icons.Default.Search, "Recherche"),
                    Triple("favorites", Icons.Default.Favorite, "Favoris")
                )

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            bottomNavItems.forEach { (route, icon, label) ->
                                NavigationBarItem(
                                    icon = { Icon(icon, contentDescription = label) },
                                    label = { Text(label) },
                                    selected = currentDestination?.hierarchy?.any { it.route == route } == true,
                                    onClick = {
                                        navController.navigate(route) {
                                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "categories",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("categories") {
                            CategoriesScreen(
                                onCategoryClick = { category ->
                                    navController.navigate("drinks/${category.replace("/", "_SLASH_")}")
                                },
                                onAlphabetClick = { navController.navigate("alphabet") },
                                onFiltersClick = { navController.navigate("filters") },
                                onIngredientInfoClick = { navController.navigate("ingredient_info") },
                                onRandomClick = { navController.navigate("detail/") }
                            )
                        }
                        composable("drinks/{category}") { backStackEntry ->
                            val category = backStackEntry.arguments?.getString("category") ?: ""
                            DrinksListScreen(
                                categoryName = category,
                                onDrinkClick = { drinkId -> navController.navigate("detail/$drinkId") }
                            )
                        }
                        composable("detail/{drinkId}") { backStackEntry ->
                            val drinkId = backStackEntry.arguments?.getString("drinkId") ?: ""
                            DetailCocktailScreen(drinkId = drinkId.ifEmpty { null })
                        }
                        composable("search") {
                            SearchScreen(onDrinkClick = { drinkId -> navController.navigate("detail/$drinkId") })
                        }
                        composable("favorites") {
                            FavoritesScreen(onDrinkClick = { drinkId -> navController.navigate("detail/$drinkId") })
                        }
                        composable("alphabet") {
                            AlphabetSearchScreen(onDrinkClick = { drinkId -> navController.navigate("detail/$drinkId") })
                        }
                        composable("filters") {
                            FiltersScreen(onDrinkClick = { drinkId -> navController.navigate("detail/$drinkId") })
                        }
                        composable("ingredient_info") {
                            IngredientInfoScreen()
                        }
                    }
                }
            }
        }
    }
}