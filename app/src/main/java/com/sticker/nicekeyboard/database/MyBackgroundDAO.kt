package com.sticker.nicekeyboard.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sticker.nicekeyboard.call_api.ImageItem

@Dao
interface MyBackgroundDAO {
    @Insert
    suspend fun insert(imageItem: ImageItem)

    @Update
    suspend fun update(imageItem: ImageItem)

    @Delete
    suspend fun delete(imageItem: ImageItem)

    @Query("SELECT * FROM my_background_table")
    fun getAllMyBackground(): LiveData<List<ImageItem>>
}