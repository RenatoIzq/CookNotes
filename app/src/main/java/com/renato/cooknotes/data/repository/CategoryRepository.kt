package com.renato.cooknotes.data.repository

import android.content.Context
import com.renato.cooknotes.data.cache.LocalCategoryCache
import com.renato.cooknotes.data.model.Category
import com.renato.cooknotes.data.model.CategoryDto
import com.renato.cooknotes.data.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryRepository(context: Context) {
    private val supabase = SupabaseClient.client
    private val localCache = LocalCategoryCache(context)

    suspend fun getCategories(): List<Category> = withContext(Dispatchers.IO) {
        val cached = localCache.getCachedCategories()

        try {
            val dtoList = supabase.from("categories").select().decodeList<CategoryDto>()
            val remote = dtoList.map { it.toDomain() }

            if (remote.isNotEmpty()) {
                localCache.saveCategories(remote)
                return@withContext remote
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (cached.isEmpty()) {
            val defaultCategories = listOf(
                Category(id = "1", name = "Postres"),
                Category(id = "2", name = "Snacks"),
                Category(id = "3", name = "Bebidas"),
                Category(id = "4", name = "Platos Principales")
            )
            localCache.saveCategories(defaultCategories)
            return@withContext defaultCategories
        }

        return@withContext cached
    }

    suspend fun addCategory(category: Category) = withContext(Dispatchers.IO) {
        val current = localCache.getCachedCategories().toMutableList()
        current.add(category)
        localCache.saveCategories(current)

        try {
            val dto = CategoryDto.fromDomain(category)
            supabase.from("categories").insert(dto)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}
