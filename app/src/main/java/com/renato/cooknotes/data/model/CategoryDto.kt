package com.renato.cooknotes.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    val id: String? = null,
    val name: String,
    val colorhex: String? = null
) {
    fun toDomain(): Category {
        return Category(
            id = id,
            name = name,
            colorHex = colorhex
        )
    }

    companion object {
        fun fromDomain(category: Category): CategoryDto {
            return CategoryDto(
                id = category.id,
                name = category.name,
                colorhex = category.colorHex
            )
        }
    }
}
