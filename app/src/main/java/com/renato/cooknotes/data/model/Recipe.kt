package com.renato.cooknotes.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: String? = null,
    val title: String,
    val description: String,
    val ingredients: String,
    val instructions: String,
    val categoryId: String,
    val imageUrl: String? = null
)
