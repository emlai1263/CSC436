package com.zybooks.collectionscataloger

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.serialization.Serializable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.zybooks.collectionscataloger.data.Item
import com.zybooks.collectionscataloger.data.ItemRepo
import com.zybooks.collectionscataloger.ui.EntryDetailScreen
import kotlinx.coroutines.launch

sealed class Routes {
    @Serializable
    data object ItemList

    @Serializable
    data object AddItem

    @Serializable
    data class EditItem (val itemId: Int)

    @Serializable
    data class ItemDetails(val itemId: Int)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogApp(
    itemRepo: ItemRepo,
//    modifier: Modifier = Modifier
) {
    val items by itemRepo.getItems().collectAsState(initial = emptyList())

    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = Routes.ItemList
    ) {
        composable<Routes.ItemList> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Collection") },
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            Log.d("CatalogApp", "Navigating to AddItem")
                            navController.navigate(Routes.AddItem)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add item"
                        )
                    }
                }
            ) { innerPadding ->
                CatalogDisplay(
                    items = items,
                    onSelect = { item ->
                        navController.navigate(Routes.ItemDetails(item.id))
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }

        composable<Routes.AddItem> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Add Item") },
                        navigationIcon = {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Add a new item"
                                )
                            }
                        },
                    )
                },
            ) { innerPadding ->
                AddItemScreen(
                    onSaveItem = { newItem ->
                        coroutineScope.launch {
                            itemRepo.addItem(newItem)
                            navController.navigateUp()
                        }
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }

        composable<Routes.ItemDetails> { backStackEntry ->
            val itemDetails: Routes.ItemDetails = backStackEntry.toRoute()
            val itemId = itemDetails.itemId

            var item by remember { mutableStateOf<Item?>(null) }

            LaunchedEffect(itemId) {
                item = itemRepo.getItemById(itemId)
            }

            Scaffold (
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        title = { Text("Details") },
                        navigationIcon = {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Localized description"
                                )
                            }
                        },
                        actions = {
                            // Edit button
                            IconButton(onClick = {
                                item?.let {
                                    navController.navigate(Routes.EditItem(it.id))
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit"
                                )
                            }
                            IconButton(onClick = {
                                item?.let {
                                    coroutineScope.launch {
                                        itemRepo.deleteItem(it) // Delete the item
                                        navController.popBackStack(Routes.ItemList, inclusive = false) // Navigate back to the list
                                    }
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete"
                                )
                            }
                        }
                    )
                }
            )
            { innerPadding ->
                if (item != null) {

                    val tagsList = item!!.tags
                        .split(" ")
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }

                    item!!.imgId?.let {
                        EntryDetailScreen(
                            imgId = it,
                            title = item!!.title,
                            tags = tagsList,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                } else {
                    Text(
                        text = "Item not found",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        composable<Routes.EditItem> { backStackEntry ->
            val editItem: Routes.EditItem = backStackEntry.toRoute()
            val itemId = editItem.itemId

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        title = { Text("Edit Item") },
                        navigationIcon = {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    )
                }
            ) { innerPadding ->
                EditItemScreen(
                    itemId = itemId,
                    itemRepo = itemRepo,
                    onSave = {
                        // Navigate back to the details screen after saving
                        navController.navigateUp()
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }

    }
}

