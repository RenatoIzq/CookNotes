package com.renato.cooknotes.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.renato.cooknotes.data.model.Category
import com.renato.cooknotes.data.model.Recipe
import com.renato.cooknotes.ui.components.AppTopBar
import com.renato.cooknotes.ui.theme.CookNotesTheme

@Composable
fun RecipeDetailScreen(
    recipe: Recipe?,
    categories: List<Category> = emptyList(),
    onBack: () -> Unit = {}
) {
    val categoryName = categories.find { it.id == recipe?.categoryId }?.name ?: "General"

    Scaffold(
        topBar = {
            AppTopBar(
                title = recipe?.title ?: "Detalle de Receta",
                showBackIcon = true,
                onBackClick = onBack
            )
        }
    ) { padding ->
        if (recipe == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Receta no encontrada")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Categoría: $categoryName",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = recipe.description,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "Ingredientes",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = recipe.ingredients,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                item {
                    Text(
                        text = "Instrucciones",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = recipe.instructions,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeDetailScreenPreview() {
    CookNotesTheme {
        RecipeDetailScreen(
            recipe = Recipe(
                title = "Pastel de Fresa Romántico",
                description = "Delicioso bizcocho con crema y fresas frescas.",
                ingredients = "• 2 tazas de harina\n• 1 taza de fresas",
                instructions = "1. Mezclar ingredientes.\n2. Hornear a 180°C.",
                categoryId = "1"
            ),
            categories = listOf(Category(id = "1", name = "Postres")),
            onBack = {}
        )
    }
}
