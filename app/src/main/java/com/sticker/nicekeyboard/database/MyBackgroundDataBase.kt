package com.sticker.nicekeyboard.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sticker.nicekeyboard.call_api.ImageItem

@Database(entities = [ImageItem::class], version = 1)
abstract class MyBackgroundDataBase : RoomDatabase() {

    abstract fun myBackgroundDao(): MyBackgroundDAO

    companion object {
        @Volatile
        private var instance: MyBackgroundDataBase? = null

        fun getInstance(context: Context): MyBackgroundDataBase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    MyBackgroundDataBase::class.java,
                    "my_background_database"
                ).build().also { instance = it }
            }
        }
    }
}