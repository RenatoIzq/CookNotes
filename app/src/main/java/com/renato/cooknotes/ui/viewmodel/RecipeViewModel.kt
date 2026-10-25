package com.renato.cooknotes.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.renato.cooknotes.data.model.Category
import com.renato.cooknotes.data.model.Recipe
import com.renato.cooknotes.data.repository.CategoryRepository
import com.renato.cooknotes.data.repository.RecipeRepository
import com.renato.cooknotes.ui.state.RecipeFormState
import kotlinx.coroutines.launch

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val recipeRepository = RecipeRepository(application)
    private val categoryRepository = CategoryRepository(application)

    var recipes by mutableStateOf<List<Recipe>>(emptyList())
        private set

    var categories by mutableStateOf<List<Category>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var selectedRecipe by mutableStateOf<Recipe?>(null)
        private set

    var formState by mutableStateOf(RecipeFormState())
        private set

    init {
        loadCategories()
        loadRecipes()
    }

    fun loadCategories() {
        viewModelScope.launch {
            try {
                categories = categoryRepository.getCategories()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadRecipes() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                recipes = recipeRepository.getRecipes()
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Error al cargar recetas"
            } finally {
                isLoading = false
            }
        }
    }

    fun addCategory(name: String, onSuccess: () -> Unit) {
        if (name.isBlank()) return
        viewModelScope.launch {
            try {
                val newCategory = Category(name = name)
                categoryRepository.addCategory(newCategory)
                loadCategories()
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun selectRecipe(recipe: Recipe?) {
        selectedRecipe = recipe
    }

    fun updateTitle(newTitle: String) {
        formState = formState.copy(title = newTitle)
    }

    fun updateDescription(newDescription: String) {
        formState = formState.copy(description = newDescription)
    }

    fun updateCategoryId(newId: String) {
        formState = formState.copy(categoryId = newId)
    }

    fun updateIngredients(newIngredients: String) {
        formState = formState.copy(ingredients = newIngredients)
    }

    fun updateInstructions(newInstructions: String) {
        formState = formState.copy(instructions = newInstructions)
    }

    fun saveRecipe(onSuccess: () -> Unit) {
        if (formState.title.isBlank()) return

        val newRecipe = Recipe(
            title = formState.title,
            description = formState.description,
            ingredients = formState.ingredients,
            instructions = formState.instructions,
            categoryId = formState.categoryId.ifBlank { categories.firstOrNull()?.id ?: "1" }
        )

        viewModelScope.launch {
            formState = formState.copy(isLoading = true, errorMessage = null)
            try {
                recipeRepository.addRecipe(newRecipe)
                loadRecipes()
                formState = RecipeFormState()
                onSuccess()
            } catch (e: Exception) {
                formState = formState.copy(
                    errorMessage = e.localizedMessage ?: "Error al guardar receta (guardada localmente)",
                    isLoading = false
                )
                recipes = recipes + newRecipe
                formState = RecipeFormState()
                onSuccess()
            } finally {
                formState = formState.copy(isLoading = false)
            }
        }
    }
}
