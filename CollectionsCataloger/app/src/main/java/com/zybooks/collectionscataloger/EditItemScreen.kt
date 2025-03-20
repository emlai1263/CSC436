package com.zybooks.collectionscataloger

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberImagePainter
import com.zybooks.collectionscataloger.data.Item
import com.zybooks.collectionscataloger.data.ItemRepo
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun EditItemScreen(
    itemId: Int,
    itemRepo: ItemRepo,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    // State to hold the edited title, tags, and image URI
    var title by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    var imgUri by remember { mutableStateOf<Uri?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // temp file
    // temp file
    val tempFile = remember {
        File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "temp_photo.jpg"
        )
    }

    // Generate a content URI using FileProvider
    val tempFileUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider", // Authority
            tempFile
        )
    }

    // Launcher for the camera app
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imgUri = tempFileUri
        }
    }

    // Launcher for requesting camera permission
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(tempFileUri)
        } else {
            Log.d("EditItemScreen", "Camera permission denied")
        }
    }

    // Fetch the item to populate the initial values
    LaunchedEffect(itemId) {
        val item = itemRepo.getItemById(itemId)
        if (item != null) {
            title = item.title
            tags = item.tags
            imgUri = Uri.parse(item.imgId)
        }
    }

    Column(modifier = modifier.padding(16.dp)) {
        Image(
            painter = if (imgUri != null) {
                rememberImagePainter(data = imgUri)
            } else {
                painterResource(id = R.drawable.ic_launcher_background)
            },
            contentDescription = "Item Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to take a new photo
        Button(
            onClick = {
                // camera perms
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(tempFileUri)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Take New Photo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Title input field
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tags input field
        TextField(
            value = tags,
            onValueChange = { tags = it },
            label = { Text("Tags (space separated)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Save button
        Button(
            onClick = {
                val updatedItem = Item(
                    id = itemId,
                    title = title,
                    tags = tags,
                    imgId = imgUri?.toString() ?: R.drawable.ic_launcher_background.toString()
                )
                Log.d("EditItemScreen", "Updated item: $updatedItem")
                // update item
                coroutineScope.launch {
                    itemRepo.updateItem(updatedItem)
                    onSave() // go back after saving
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Changes")
        }
    }
}