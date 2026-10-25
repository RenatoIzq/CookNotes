package com.renato.cooknotes.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RecipeDto(
    val id: String? = null,
    val title: String,
    val description: String,
    val ingredients: String,
    val instructions: String,
    val category_id: String,
    val image_url: String? = null
) {
    fun toDomain(): Recipe {
        return Recipe(
            id = id,
            title = title,
            description = description,
            ingredients = ingredients,
            instructions = instructions,
            categoryId = category_id,
            imageUrl = image_url
        )
    }

    companion object {
        fun fromDomain(recipe: Recipe): RecipeDto {
            return RecipeDto(
                id = recipe.id,
                title = recipe.title,
                description = recipe.description,
                ingredients = recipe.ingredients,
                instructions = recipe.instructions,
                category_id = recipe.categoryId,
                image_url = recipe.imageUrl
            )
        }
    }
}
