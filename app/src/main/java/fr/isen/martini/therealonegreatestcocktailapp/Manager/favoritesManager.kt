package fr.isen.martini.thegreatestcocktailapp

import android.content.Context

object FavoritesManager {
    private const val PREFS_NAME = "cocktail_prefs"
    private const val FAV_KEY = "favorites_list"

    fun getFavorites(context: Context): MutableSet<String> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(FAV_KEY, mutableSetOf())?.toMutableSet() ?: mutableSetOf()
    }

    fun toggleFavorite(context: Context, drinkId: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val favorites = getFavorites(context)

        if (favorites.contains(drinkId)) {
            favorites.remove(drinkId)
        } else {
            favorites.add(drinkId)
        }

        prefs.edit().putStringSet(FAV_KEY, favorites).apply()
    }
}