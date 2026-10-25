package com.renato.cooknotes.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: String? = null,
    val name: String,
    val colorHex: String? = null
)
