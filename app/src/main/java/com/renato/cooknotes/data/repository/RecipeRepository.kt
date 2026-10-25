package com.renato.cooknotes.data.repository

import android.content.Context
import com.renato.cooknotes.data.cache.LocalRecipeCache
import com.renato.cooknotes.data.model.Recipe
import com.renato.cooknotes.data.model.RecipeDto
import com.renato.cooknotes.data.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeRepository(context: Context) {
    private val supabase = SupabaseClient.client
    private val localCache = LocalRecipeCache(context)

    suspend fun getRecipes(): List<Recipe> = withContext(Dispatchers.IO) {
        // 1. Obtener caché local primero (Offline-First / Carga instantánea)
        val cachedRecipes = localCache.getCachedRecipes()

        try {
            // 2. Consultar a Supabase para verificar si hay nuevas recetas o modificaciones
            val dtoList = supabase.from("recipes").select().decodeList<RecipeDto>()
            val remoteRecipes = dtoList.map { it.toDomain() }

            if (remoteRecipes.isNotEmpty()) {
                // 3. Si Supabase tiene datos, sobrescribimos la caché local con la nueva versión
                localCache.saveRecipes(remoteRecipes)
                return@withContext remoteRecipes
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Si falla la red (sin internet), devolvemos lo que teníamos en caché local
        }

        // Si Supabase está vacío o sin conexión, devolvemos caché o fallback demo
        if (cachedRecipes.isEmpty()) {
            val defaultRecipes = listOf(
                Recipe(
                    id = "1",
                    title = "Pastel de Fresa Romántico",
                    description = "Un delicioso bizcocho esponjoso con crema de fresas y toque rosado pastel.",
                    ingredients = "• 2 tazas de harina\n• 1 taza de azúcar\n• 1/2 taza de mantequilla\n• Fresas frescas",
                    instructions = "1. Mezclar ingredientes secos.\n2. Incorporar mantequilla y fresas.\n3. Hornear a 180°C por 35 minutos.",
                    categoryId = "1"
                ),
                Recipe(
                    id = "2",
                    title = "Galletas de Vainilla Pastel",
                    description = "Galletas crujientes por fuera y suaves por dentro con glaseado suave.",
                    ingredients = "• 250g mantequilla\n• 100g azúcar glass\n• 300g harina",
                    instructions = "1. Batir mantequilla y azúcar.\n2. Añadir harina y formar masa.\n3. Hornear 15 min.",
                    categoryId = "2"
                )
            )
            localCache.saveRecipes(defaultRecipes)
            return@withContext defaultRecipes
        }

        return@withContext cachedRecipes
    }

    suspend fun addRecipe(recipe: Recipe) = withContext(Dispatchers.IO) {
        // 1. Guardar localmente en caché primero (Soporte offline)
        val currentCached = localCache.getCachedRecipes().toMutableList()
        currentCached.add(0, recipe)
        localCache.saveRecipes(currentCached)

        // 2. Intentar subir a Supabase
        try {
            val dto = RecipeDto.fromDomain(recipe)
            supabase.from("recipes").insert(dto)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}
