package com.sticker.nicekeyboard.call_api


data class CategoryWithImages(
    val category: String,
    val images: List<ImageItem>,
    val hasMore: Boolean,
    val isAds: Boolean = false
)