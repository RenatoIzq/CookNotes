package com.renato.cooknotes.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.renato.cooknotes.data.model.Recipe
import com.renato.cooknotes.ui.components.AppTopBar
import com.renato.cooknotes.ui.components.RecipeCard
import com.renato.cooknotes.ui.viewmodel.RecipeViewModel

@Composable
fun RecipeListScreen(
    viewModel: RecipeViewModel,
    onNavigateToDetail: (Recipe) -> Unit,
    onNavigateToAdd: () -> Unit,
    onNavigateToCategories: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = "CookNotes 🌸",
                actions = {
                    IconButton(onClick = onNavigateToCategories) {
                        Icon(Icons.Default.Category, contentDescription = "Gestionar Categorías")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAdd,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Receta")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (viewModel.isLoading && viewModel.recipes.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (viewModel.recipes.isEmpty()) {
                Text(
                    text = "No hay recetas aún. ¡Agrega la primera!",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onBackground
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(viewModel.recipes) { recipe ->
                        val categoryName = viewModel.categories.find { it.id == recipe.categoryId }?.name ?: "General"
                        RecipeCard(recipe = recipe, categoryName = categoryName, onClick = {
                            viewModel.selectRecipe(recipe)
                            onNavigateToDetail(recipe)
                        })
                    }
                }
            }
        }
    }
}
