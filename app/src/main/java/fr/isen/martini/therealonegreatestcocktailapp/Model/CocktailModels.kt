package fr.isen.martini.thegreatestcocktailapp

import java.io.Serializable

data class CocktailResponse(
    val drinks: List<CocktailDetail>?
) : Serializable

data class CocktailDetail(
    val idDrink: String,
    val strDrink: String,
    val strDrinkThumb: String,
    val strInstructions: String,
    val strCategory: String,
    val strGlass: String,
    val strIngredient1: String?,
    val strMeasure1: String?
) : Serializable

data class IngredientResponse(val ingredients: List<IngredientDetail>?)

data class IngredientDetail(
    val idIngredient: String,
    val strIngredient: String,
    val strDescription: String?,
    val strType: String?,
    val strAlcohol: String?
)

data class CategoryResponse(val drinks: List<Category>) : Serializable
data class Category(val strCategory: String) : Serializable

data class DrinkResponse(val drinks: List<DrinkBrief>) : Serializable
data class DrinkBrief(
    val strDrink: String,
    val strDrinkThumb: String,
    val idDrink: String
) : Serializable