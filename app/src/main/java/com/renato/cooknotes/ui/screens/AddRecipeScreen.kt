package com.renato.cooknotes.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.renato.cooknotes.ui.components.AppTopBar
import com.renato.cooknotes.ui.components.PrimaryButton
import com.renato.cooknotes.ui.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(
    viewModel: RecipeViewModel,
    onBack: () -> Unit
) {
    val formState = viewModel.formState
    val categories = viewModel.categories
    var expanded by remember { mutableStateOf(false) }

    val selectedCategoryName = categories.find { it.id == formState.categoryId }?.name
        ?: categories.firstOrNull()?.name ?: "Seleccionar categoría"

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Nueva Receta 🌸",
                showBackIcon = true,
                onBackClick = onBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                OutlinedTextField(
                    value = formState.title,
                    onValueChange = { viewModel.updateTitle(it) },
                    label = { Text("Título de la Receta") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
            item {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedCategoryName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    category.id?.let { viewModel.updateCategoryId(it) }
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
            item {
                OutlinedTextField(
                    value = formState.description,
                    onValueChange = { viewModel.updateDescription(it) },
                    label = { Text("Descripción breve") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 3
                )
            }
            item {
                OutlinedTextField(
                    value = formState.ingredients,
                    onValueChange = { viewModel.updateIngredients(it) },
                    label = { Text("Ingredientes (uno por línea)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 3
                )
            }
            item {
                OutlinedTextField(
                    value = formState.instructions,
                    onValueChange = { viewModel.updateInstructions(it) },
                    label = { Text("Instrucciones de preparación") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 4
                )
            }
            item {
                PrimaryButton(
                    text = if (formState.isLoading) "Guardando..." else "Guardar Receta",
                    onClick = {
                        viewModel.saveRecipe(onSuccess = onBack)
                    }
                )
            }
        }
    }
}
