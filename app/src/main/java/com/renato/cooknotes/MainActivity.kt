package com.renato.cooknotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.renato.cooknotes.ui.screens.AddRecipeScreen
import com.renato.cooknotes.ui.screens.ManageCategoriesScreen
import com.renato.cooknotes.ui.screens.RecipeDetailScreen
import com.renato.cooknotes.ui.screens.RecipeListScreen
import com.renato.cooknotes.ui.theme.CookNotesTheme
import com.renato.cooknotes.ui.viewmodel.RecipeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CookNotesTheme {
                val viewModel: RecipeViewModel = viewModel()
                var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.List) }

                when (currentScreen) {
                    is AppScreen.List -> {
                        RecipeListScreen(
                            viewModel = viewModel,
                            onNavigateToDetail = { currentScreen = AppScreen.Detail },
                            onNavigateToAdd = { currentScreen = AppScreen.Add },
                            onNavigateToCategories = { currentScreen = AppScreen.Categories }
                        )
                    }
                    is AppScreen.Detail -> {
                        RecipeDetailScreen(
                            recipe = viewModel.selectedRecipe,
                            categories = viewModel.categories,
                            onBack = { currentScreen = AppScreen.List }
                        )
                    }
                    is AppScreen.Add -> {
                        AddRecipeScreen(
                            viewModel = viewModel,
                            onBack = { currentScreen = AppScreen.List }
                        )
                    }
                    is AppScreen.Categories -> {
                        ManageCategoriesScreen(
                            viewModel = viewModel,
                            onBack = { currentScreen = AppScreen.List }
                        )
                    }
                }
            }
        }
    }
}

sealed class AppScreen {
    object List : AppScreen()
    object Detail : AppScreen()
    object Add : AppScreen()
    object Categories : AppScreen()
}
