package com.zybooks.collectionscataloger.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM item")
    fun getItems(): Flow<List<Item>>

    @Query("SELECT * FROM item WHERE id = :itemId")
    suspend fun getItemById(itemId: Int): Item?

    @Insert
    fun addItem(item: Item)

    @Update
    suspend fun updateItem(item: Item)

    @Delete
    suspend fun deleteItem(item: Item)

//    ----- unused below --------

    @Query("SELECT * FROM item ORDER BY title ASC")
    fun orderByTitle(): Flow<List<Item>>

    @Query("SELECT * FROM item ORDER BY timestamp DESC")
    fun orderByTimestamp(): Flow<List<Item>>

    @Query("SELECT * FROM item WHERE tags GLOB '%' || :query || '%' OR title GLOB '%' || :query || '%'")
    fun searchItems(query: String): Flow<List<Item>>
}