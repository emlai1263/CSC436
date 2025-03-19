package com.zybooks.collectionscataloger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zybooks.collectionscataloger.ui.theme.CollectionsCatalogerTheme
import com.zybooks.collectionscataloger.data.Item
import com.zybooks.collectionscataloger.data.ItemRepo

class MainActivity : ComponentActivity() {
    private lateinit var itemRepo: ItemRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        itemRepo = ItemRepo(applicationContext)

        enableEdgeToEdge()

        setContent {
            CollectionsCatalogerTheme {
                CatalogApp(itemRepo = itemRepo)
            }
        }
    }
}