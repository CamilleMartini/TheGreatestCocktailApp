package fr.isen.martini.thegreatestcocktailapp

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("list.php?c=list")
    suspend fun getCategories(): CategoryResponse

    @GET("filter.php")
    suspend fun getDrinksByCategory(@Query("c") category: String): DrinkResponse

    @GET("random.php")
    suspend fun getRandomCocktail(): CocktailResponse

    @GET("lookup.php")
    suspend fun getDrinkDetail(@Query("i") id: String): CocktailResponse

    @GET("search.php")
    suspend fun searchCocktails(@Query("s") name: String): DrinkResponse

    @GET("filter.php")
    suspend fun filterByIngredient(@Query("i") ingredient: String): DrinkResponse

    @GET("search.php")
    suspend fun getDrinksByFirstLetter(@Query("f") letter: String): DrinkResponse

    @GET("search.php")
    suspend fun getIngredientDetails(@Query("i") name: String): IngredientResponse

    // Dans ApiService.kt
    @GET("filter.php")
    suspend fun filterByAlcoholic(@Query("a") type: String): DrinkResponse

    @GET("filter.php")
    suspend fun filterByCategory(@Query("c") category: String): DrinkResponse

    @GET("filter.php")
    suspend fun filterByGlass(@Query("g") glass: String): DrinkResponse

}

