package com.sticker.nicekeyboard.ui.fragment_theme

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sticker.nicekeyboard.R
import com.sticker.nicekeyboard.ui.fragment_theme.category.CategoryModel
import com.sticker.nicekeyboard.ui.fragment_theme.theme.ThemeModel
import com.sticker.nicekeyboard.util.Constants

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentThemeViewModel : ViewModel() {
    private val _categories = MutableLiveData<List<CategoryModel>>()
    val categories: LiveData<List<CategoryModel>> get() = _categories

    private suspend fun getListIconFromAssetFolder(
        context: Context,
        folderName: String,
        backgroundFolderName: String
    ): List<ThemeModel> {
        return withContext(Dispatchers.IO) {
            val themeList = mutableListOf<ThemeModel>()
            val assetManager: AssetManager = context.assets
            val files = assetManager.list(folderName)
            val backgroundFiles = assetManager.list(backgroundFolderName)

            if (files != null && backgroundFiles != null) {
                for ((index, file) in files.withIndex()) {
                    val themeName = when (file) {
                        "style_ios111.png" -> Constants.THEME_1
                        "style_ios112.png" -> Constants.THEME_2
                        "style_ios113.png" -> Constants.THEME_3
                        "style_ios114.png" -> Constants.THEME_4
                        "style_ios115.png" -> Constants.THEME_5
                        "style_ios116.png" -> Constants.THEME_6
                        "style_ios117.png" -> Constants.THEME_7
                        "style_ios118.png" -> Constants.THEME_8
                        "style_ios119.png" -> Constants.THEME_9
                        "style_ios120.png" -> Constants.THEME_10
                        "style_ios121.png" -> Constants.THEME_11
                        "style_ios122.png" -> Constants.THEME_12
                        "style_ios123.png" -> Constants.THEME_13
                        "style_ios124.png" -> Constants.THEME_14
                        "style_ios125.png" -> Constants.THEME_15
                        "style_ios126.png" -> Constants.THEME_16
                        "style_ios127.png" -> Constants.THEME_17
                        "style_ios128.png" -> Constants.THEME_18
                        "style_ios129.png" -> Constants.THEME_19
                        "style_ios130.png" -> Constants.THEME_20
                        "style_ios131.png" -> Constants.THEME_21
                        "style_ios132.png" -> Constants.THEME_22
                        "style_ios133.png" -> Constants.THEME_23
                        "style_ios134.png" -> Constants.THEME_24
                        "style_ios135.png" -> Constants.THEME_25
                        "style_ios136.png" -> Constants.THEME_26
                        "style_ios137.png" -> Constants.THEME_27
                        "style_ios138.png" -> Constants.THEME_28
                        "style_ios139.png" -> Constants.THEME_29
                        "style_ios140.png" -> Constants.THEME_30
                        "style_ios141.png" -> Constants.THEME_31
                        "style_ios150.png" -> Constants.THEME_32
                        "style_ios151.png" -> Constants.THEME_33
                        "style_ios152.png" -> Constants.THEME_34
                        "style_ios153.png" -> Constants.THEME_35
                        "style_ios154.png" -> Constants.THEME_36
                        "style_ios155.png" -> Constants.THEME_37
                        "style_ios161.png" -> Constants.THEME_38
                        "style_ios162.png" -> Constants.THEME_39
                        "style_ios163.png" -> Constants.THEME_40
                        "style_ios164.png" -> Constants.THEME_41
                        "style_ios165.png" -> Constants.THEME_42
                        "style_ios166.png" -> Constants.THEME_43
                        "style_ios167.png" -> Constants.THEME_44
                        "style_ios168.png" -> Constants.THEME_45
                        else -> Constants.THEME_15
                    }

                    if (file.endsWith(".png") || file.endsWith(".jpg") || file.endsWith(".jpeg")) {
                        val imagePath = "file:///android_asset/$folderName/$file"
                        val backgroundPath = "$backgroundFolderName/${backgroundFiles[index]}"
                        themeList.add(
                            ThemeModel(
                                index,
                                imagePath,
                                backgroundPath,
                                themeName,
                                false
                            )
                        )
                    }
                    Log.d("aaa", themeName)
                }
            }
            themeList
        }
    }

    fun loadThemesForCategories(context: Context, categoryList: List<CategoryModel>) {
        viewModelScope.launch {
            val allThemes = mutableListOf<ThemeModel>()
            val updatedCategories = categoryList.map { category ->
                val themes = getListIconFromAssetFolder(
                    context,
                    category.folderName,
                    category.folderBackground
                )
                allThemes.addAll(themes)
                Log.d("ThemesLoaded", "Category: ${category.categoryName}, Themes: $themes")
                category.copy(listThemeModel = themes)
            }

            val allCategory = CategoryModel(
                categoryName = context.getString(R.string.all_theme),
                isSelected = true,
                folderName = Constants.ALL_THEME,
                folderBackground = Constants.ALL_THEME_BG,
                listThemeModel = allThemes
            )

            val finalCategories = listOf(allCategory) + updatedCategories
            _categories.postValue(finalCategories)  // Use postValue to ensure main thread update
        }
    }
}