package com.zybooks.collectionscataloger.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String = "",
    var tags: String = "",
    val imgId: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)