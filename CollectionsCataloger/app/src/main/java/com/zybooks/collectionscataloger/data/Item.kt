package com.zybooks.collectionscataloger.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String = "",
    var tags: String = "",
    val imgId: String? = null, // should be img's uri
    val timestamp: Long = System.currentTimeMillis() // for sorting by upload order (not implemented)
)