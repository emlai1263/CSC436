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
//                val items = viewModel.items.collectAsState(initial = emptyList())
                CatalogApp(itemRepo = itemRepo)
//                )

//                AddItemScreen(onSaveItem = { newItem ->
//                    Log.d("MainActivity", "onSaveItem called with: $newItem")
//                    viewModel.onItemEvent(ItemEvent.SaveItem)
//                })

//                Text(itemDao.getItems().toString())
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CollectionsCatalogerTheme {
        Greeting("Android")
    }
}