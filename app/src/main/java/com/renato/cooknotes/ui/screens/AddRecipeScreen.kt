package com.renato.cooknotes.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
    var currentIngredient by remember { mutableStateOf("") }
    val ingredientList = remember { mutableStateListOf<String>() }
    var fase by remember { mutableStateOf("") }
    val listFases = remember { mutableStateListOf<String>() }

    val selectedCategoryName = categories.find { it.id == formState.categoryId }?.name
        ?: categories.firstOrNull()?.name ?: "Seleccionar categoría"

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Nueva Receta",
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = currentIngredient,
                        onValueChange = { currentIngredient = it },
                        label = { Text("Ingrediente") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    IconButton(
                        onClick = {
                            if (currentIngredient.isNotBlank()) {
                                ingredientList.add(currentIngredient.trim())
                                viewModel.updateIngredients(ingredientList.joinToString("\n"))
                                currentIngredient = ""
                            }
                        }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar ingrediente")
                    }
                }
            }
            if (ingredientList.isNotEmpty()) {
                item {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ingredientList.forEach { ingredient ->
                            InputChip(
                                selected = false,
                                onClick = { },
                                label = { Text(ingredient) },
                                trailingIcon = {
                                    IconButton(
                                        onClick = {
                                            ingredientList.remove(ingredient)
                                            viewModel.updateIngredients(ingredientList.joinToString("\n"))
                                        },
                                        modifier = Modifier.size(18.dp)
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = "Eliminar")
                                    }
                                }
                            )
                        }
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = fase,
                        onValueChange = { fase = it },
                        label = { Text("Descripción de por paso") },
                        modifier = Modifier.weight(1f),
                        maxLines = 5,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                IconButton(
                    onClick = {
                        if (fase.isNotBlank()) {
                            listFases.add(fase.trim())
                            fase = ""
                        }
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar paso")
                }
                if (listFases.isNotEmpty()) {
                    this@LazyColumn.item {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listFases.forEach { fase ->
                                InputChip(
                                    selected = false,
                                    onClick = { },
                                    label = { Text(fase) },
                                    modifier = Modifier.fillMaxWidth(),
                                    trailingIcon = {
                                        IconButton(
                                            onClick = {
                                                listFases.remove(fase)
                                            },
                                            modifier = Modifier.size(18.dp)
                                        ) {
                                            Icon(Icons.Default.Close, contentDescription = "Eliminar")
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

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
