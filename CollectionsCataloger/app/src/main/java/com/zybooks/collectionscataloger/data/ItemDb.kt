package com.zybooks.collectionscataloger.data

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room

@Database(
    entities = [Item::class],
    version = 2
)

// initializing db only once
abstract class ItemDb: RoomDatabase() {
    abstract val itemDao: ItemDao

    companion object { // allows calling getDatabase w/o instantiating an ItemDb
        @Volatile
        private var INSTANCE: ItemDb? = null

        fun getDatabase(context: Context): ItemDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemDb::class.java,
                    "item_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}