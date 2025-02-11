package com.zybooks.petadoption.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zybooks.petadoption.data.Pet
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.zybooks.petadoption.data.PetDataSource
import com.zybooks.petadoption.data.PetGender
import com.zybooks.petadoption.ui.theme.PetAdoptionTheme
import kotlinx.serialization.Serializable

sealed class Routes {
   @Serializable
   data object List

   @Serializable
   data class Detail(
      val petId: Int
   )

   @Serializable
   data class Adopt(
      val petId: Int
   )
}

@Composable
fun PetApp() {
   val navController = rememberNavController()

   NavHost(
      navController = navController,
      startDestination = Routes.List
   ) {
      composable<Routes.List> {
         ListScreen(
            onImageClick = { pet ->
               navController.navigate(
                  Routes.Detail(pet.id)
               )
            }
         )
      }
      composable<Routes.Detail> { backstackEntry ->
         val details: Routes.Detail = backstackEntry.toRoute()

         DetailScreen(
            petId = details.petId,
            onAdoptClick = {
               navController.navigate(
                  Routes.Adopt(details.petId)
               )
            },
            onUpClick = {
               navController.navigateUp()
            }
         )
      }
      composable<Routes.Adopt> { backstackEntry ->
         val adopt: Routes.Adopt = backstackEntry.toRoute()

         AdoptScreen(
            petId = adopt.petId,
            onUpClick = {
               navController.navigateUp()
            }
         )
      }
   }
}

@Composable
fun AdoptScreen(
   petId: Int,
//   pet: Pet,
   modifier: Modifier = Modifier,
   viewModel: AdoptViewModel = viewModel(),
   onUpClick: () -> Unit = { }
) {
   val pet = viewModel.getPet(petId)

   Scaffold(
      topBar = {
         PetAppBar(
            title = "Thank You!",
            canNavigateBack = true,
            onUpClick = onUpClick
         )
      }
   ) {
   }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetAppBar(
   title: String,
   modifier: Modifier = Modifier,
   canNavigateBack: Boolean = false,
   onUpClick: () -> Unit = { },
) {
   TopAppBar(
      title = { Text(title) },
      colors = TopAppBarDefaults.topAppBarColors(
         containerColor = MaterialTheme.colorScheme.primaryContainer
      ),
      modifier = modifier,
      navigationIcon = {
         if (canNavigateBack) {
            IconButton(onClick = onUpClick) {
               Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
         }
      }
   )
}

@Composable
fun ListScreen(
   onImageClick: (Pet) -> Unit,
   modifier: Modifier = Modifier,
   viewModel: ListViewModel = viewModel()
) {
   Scaffold(
      topBar = {
         PetAppBar(
            title = "Find a Friend"
         )
      }
   ) { innerPadding ->
      LazyVerticalGrid(
         columns = GridCells.Adaptive(minSize = 128.dp),
         contentPadding = PaddingValues(0.dp),
         modifier = modifier.padding(innerPadding)
      ) {
         items(viewModel.petList) { pet ->
            Image(
               painter = painterResource(id = pet.imageId),
               contentDescription = "${pet.type} ${pet.gender}",
               modifier = Modifier.clickable(
                  onClick = { onImageClick(pet) },
                  onClickLabel = "Select the pet"
               )
            )
         }
      }
   }
}

@Preview
@Composable
fun PreviewListScreen() {
   PetAdoptionTheme {
      ListScreen(
//         petList = PetDataSource().loadPets(),
         onImageClick = { }
      )
   }
}

@Composable
fun DetailScreen(
   petId: Int,
//   pet: Pet,
   onAdoptClick: () -> Unit,
   viewModel: DetailViewModel = viewModel(),
   modifier: Modifier = Modifier,
   onUpClick: () -> Unit = { }
) {
   val pet = viewModel.getPet(petId)
   val gender = if (pet.gender == PetGender.MALE) "Male" else "Female"

   Scaffold(
      topBar = {
         PetAppBar(
            title = "Details",
            canNavigateBack = true,
            onUpClick = onUpClick
         )
      }
   ) { innerPadding ->
      Column(
         modifier = modifier.padding(innerPadding)
      ) {
         Image(
            painter = painterResource(pet.imageId),
            contentDescription = pet.name,
            contentScale = ContentScale.FillWidth
         )
         Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = modifier.padding(6.dp)
         ) {
            Row(
               horizontalArrangement = Arrangement.SpaceBetween,
               verticalAlignment = Alignment.CenterVertically,
               modifier = modifier.fillMaxWidth()
            ) {
               Text(
                  text = pet.name,
                  style = MaterialTheme.typography.headlineMedium
               )
               Button(onClick = onAdoptClick) {
                  Text("Adopt Me!")
               }
            }
            Text(
               text = "Gender: $gender",
               style = MaterialTheme.typography.bodyLarge
            )
            Text(
               text = "Age: ${pet.age}",
               style = MaterialTheme.typography.bodyLarge
            )
            Text(
               text = pet.description,
               style = MaterialTheme.typography.bodyMedium
            )
         }
      }
   }
}

@Preview
@Composable
fun PreviewDetailScreen() {
   val pet = PetDataSource().loadPets()[0]
   PetAdoptionTheme {
      DetailScreen(
         petId = pet.id,
         onAdoptClick = { }
      )
   }
}

@Preview
@Composable
fun PreviewAdoptScreen() {
   val pet = PetDataSource().loadPets()[0]
   PetAdoptionTheme {
      AdoptScreen(pet.id)
   }
}