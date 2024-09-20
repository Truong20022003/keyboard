package com.sticker.nicekeyboard.call_api

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_background_table")
data class ImageItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val thumbnail: String,
    val image: String,
    val category: String
)
