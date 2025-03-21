package com.zybooks.collectionscataloger

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberImagePainter
import com.zybooks.collectionscataloger.data.Item
import java.io.File
import android.Manifest

@Composable
fun AddItemScreen(
    onSaveItem: (Item) -> Unit,
    modifier: Modifier
) {
    var title by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    var imgUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    // create a file for the photo
    val photoFile by remember {
        mutableStateOf(
            File.createTempFile(
                "photo_",
                ".jpg",
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            )
        )
    }

    // create a URI for the photo file
    val photoUriForCamera = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )
    }

    // launcher for the camera app
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imgUri = photoUriForCamera
        }
    }

    // launcher for requesting camera permission
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(photoUriForCamera)
        } else {
            Log.d("AddItemScreen", "Camera permission denied")
        }
    }

    Column(
        modifier = modifier
    ) {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = tags,
            onValueChange = { tags = it },
            label = { Text("Tags (space separated)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // check camera permission
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(photoUriForCamera)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Take Photo")
        }

        // display the captured photo
        imgUri?.let { uri ->
            Image(
                painter = rememberImagePainter(uri),
                contentDescription = "Captured Photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val newItem = Item(
                    id = 0, // auto-generate id
                    title = title,
                    tags = tags,
                    imgId = imgUri.toString()
                )
                Log.d("MainActivity", "onSaveItem called with: $newItem")
                onSaveItem(newItem)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Item")
        }
    }
}