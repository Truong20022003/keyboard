package com.sticker.nicekeyboard.call_api



import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("api/v024_keyboard_theme_bepic/all")
    suspend fun getImageItems(): List<ImageItem>

    @GET("api/v024_keyboard_theme_bepic/category/{category}")
    suspend fun getImageItemsForCategory(@Path("category") category: String): List<ImageItem>

    //theem
}