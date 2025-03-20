package com.zybooks.collectionscataloger.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ItemRepo(context: Context) {
   private val db = ItemDb.getDatabase(context)
   private val itemDao = db.itemDao

   fun getItems(): Flow<List<Item>> = itemDao.getItems()

   suspend fun getItemById(itemId: Int): Item? {
      return withContext(Dispatchers.IO) {
         itemDao.getItemById(itemId)
      }
   }

   suspend fun addItem(item: Item) {
      withContext(Dispatchers.IO) {
         itemDao.addItem(item)
      }
   }

   suspend fun updateItem(item: Item) {
      withContext(Dispatchers.IO) {
         itemDao.updateItem(item)
      }
   }

   suspend fun deleteItem(item: Item) {
      withContext(Dispatchers.IO) {
         itemDao.deleteItem(item)
      }
   }
}
