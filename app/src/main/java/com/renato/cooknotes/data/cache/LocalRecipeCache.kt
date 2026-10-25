package com.renato.cooknotes.data.cache

import android.content.Context
import com.renato.cooknotes.data.model.Recipe
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class LocalRecipeCache(context: Context) {
    private val file = File(context.filesDir, "recipes_cache.json")
    private val json = Json { ignoreUnknownKeys = true }

    fun getCachedRecipes(): List<Recipe> {
        return try {
            if (file.exists()) {
                val content = file.readText()
                json.decodeFromString<List<Recipe>>(content)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun saveRecipes(recipes: List<Recipe>) {
        try {
            val content = json.encodeToString(recipes)
            file.writeText(content)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
