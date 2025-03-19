package com.zybooks.collectionscataloger.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.zybooks.collectionscataloger.R
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode

@Composable
fun EntryDetailScreen(
    imgId: String,
    title: String,
    tags: List<String>,
    modifier: Modifier = Modifier
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        // Display the image as a square with the same width as the screen
        Image(
            painter = rememberImagePainter(
                data = imgId, // URI or URL of the image
                builder = {
                    crossfade(true)
                    placeholder(R.drawable.ic_launcher_background) // Placeholder while loading
                    error(R.drawable.ic_launcher_background) // Error image if loading fails
                }
            ),
            contentDescription = title,
            modifier = Modifier
                .fillMaxWidth()
                .height(screenWidth) // Make the height equal to the screen width
                .clip(RoundedCornerShape(8.dp)), // Rounded corners for the image
            contentScale = ContentScale.Crop // Crop the image to fit the container
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        TagsRow(tags)
    }
}

@Composable
fun TagsRow(tags: List<String>) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        mainAxisSpacing = 4.dp, // Small horizontal spacing between tags
        crossAxisSpacing = 4.dp, // Small vertical spacing between tags
        mainAxisAlignment = MainAxisAlignment.Start,
        mainAxisSize = SizeMode.Expand
    ) {
        tags.forEach { tag ->
            Text(
                text = "#$tag",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}