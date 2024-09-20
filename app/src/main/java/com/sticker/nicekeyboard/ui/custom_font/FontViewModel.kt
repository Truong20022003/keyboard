package com.sticker.nicekeyboard.ui.custom_font

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class FontViewModel : ViewModel() {

    private val _fontList = MutableLiveData<List<FontModel>>()
    val fontList: LiveData<List<FontModel>> get() = _fontList
    fun loadThemes(context: Context, folderName: String) {
        viewModelScope.launch {
            try {
                val themeList = getListIconFromAssetFolder(context, folderName)
                _fontList.value = themeList
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    private suspend fun getListIconFromAssetFolder(context: Context, folderName: String): List<FontModel> {
        return withContext(Dispatchers.IO) {
            val imagePaths = mutableListOf<FontModel>()
            val assetManager: AssetManager = context.assets
            val files = assetManager.list(folderName)
            if (files != null) {
                for ((index, file) in files.withIndex()) {
                    val themeName = file.substringBeforeLast('.')
                    imagePaths.add(FontModel(index,  themeName,"file:///android_asset/$folderName/$file"))

                }
            }
            Log.d("aaa",Thread.currentThread().name+"_______")
            imagePaths
        }
    }
}