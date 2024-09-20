package com.sticker.nicekeyboard.ui.fragment_theme.category

import com.sticker.nicekeyboard.ui.fragment_theme.theme.ThemeModel


data class CategoryModel(
    var categoryName: String,
    var isSelected: Boolean,
    var folderName: String,
    var folderBackground: String,
    var listThemeModel: List<ThemeModel>
)