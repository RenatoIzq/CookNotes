package com.renato.cooknotes.data.cache

import android.content.Context
import com.renato.cooknotes.data.model.Category
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class LocalCategoryCache(context: Context) {
    private val file = File(context.filesDir, "categories_cache.json")
    private val json = Json { ignoreUnknownKeys = true }

    fun getCachedCategories(): List<Category> {
        return try {
            if (file.exists()) {
                val content = file.readText()
                json.decodeFromString<List<Category>>(content)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun saveCategories(categories: List<Category>) {
        try {
            val content = json.encodeToString(categories)
            file.writeText(content)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
