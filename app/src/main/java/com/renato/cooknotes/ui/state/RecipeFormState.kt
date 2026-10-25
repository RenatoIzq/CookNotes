package com.renato.cooknotes.ui.state

data class RecipeFormState(
    val title: String = "",
    val description: String = "",
    val categoryId: String = "",
    val ingredients: String = "",
    val instructions: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
