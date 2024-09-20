package com.sticker.nicekeyboard.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat

object Constants {
    const val BUNDLE_SPLASH_NO_INTERNET = "BUNDLE_SPLASH_NO_INTERNET"
    const val BUNDLE_SPLASH = "BUNDLE_SPLASH"
    const val THEME_POSITION = 0
    const val CUSTOM_POSITION = 1
    const val SETTING_POSITION = 2
    const val BUNDLE_THEME_LIST = "BUNDLE_THEME_LIST"

    const val BUNDLE_POSITION = "BUNDLE_POSITION"
    const val BUNDLE_FOLDER_NAME = "BUNDLE_FOLDER_NAME"
    const val BUNDLE_FOLDER_BACKGROUND = "BUNDLE_FOLDER_BACKGROUND"

    const val MY_BACKGROUND = "file:///android_asset/img_camera.png"


    const val ALL_THEME = "all_theme"
    const val BASIC_THEME = "style_theme/basic_theme"
    const val CHRISTMAS_THEME = "style_theme/christmas_theme"
    const val CUTE_THEME = "style_theme/cute_theme"
    const val HALLOWEEN_THEME = "style_theme/halloween_theme"
    const val NEON_THEME = "style_theme/neon_theme"
    const val NEW_YEAR_THEME = "style_theme/newyear"

    const val ALL_THEME_BG = "all_theme_bg"
    const val BASIC_THEME_BG = "style_background/basic_theme"
    const val CHRISTMAS_THEME_BG = "style_background/christmas_theme"
    const val CUTE_THEME_BG = "style_background/cute_theme"
    const val HALLOWEEN_THEME_BG = "style_background/halloween_theme"
    const val NEON_THEME_BG = "style_background/neon_theme"
    const val NEW_YEAR_THEME_BG = "style_background/newyear"

    fun hideKeyboard(activity: Activity, edtTest: EditText) {
        (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            edtTest.windowToken,
            0
        )
    }

    fun hideKeyboard2(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    fun isMyKeyboardEnabled(context: Context): Boolean {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val enabledInputMethodList = inputMethodManager.enabledInputMethodList
        for (inputMethodInfo in enabledInputMethodList) {
            if (inputMethodInfo.packageName == context.packageName) {
                return true
            }
        }
        return false
    }

    fun isMyKeyboardActive(context: Context): Boolean {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val enabledInputMethodList = inputMethodManager.enabledInputMethodList
        val defaultInputMethodId = Settings.Secure.getString(context.contentResolver, Settings.Secure.DEFAULT_INPUT_METHOD)
        for (inputMethodInfo in enabledInputMethodList) {
            if (inputMethodInfo.packageName == context.packageName && inputMethodInfo.id == defaultInputMethodId) {
                return true
            }
        }
        return false
    }
    fun checkStoragePermission(context: Context): Boolean {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                return ContextCompat.checkSelfPermission(
                    context, Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
            }
            return false
        }
    }

    const val ENABLE_SOUND: String = "enable_sound"
    const val IKEYBOARD: String = "ikeyboard"

    const val THEME_1: String = "theme_1"
    const val THEME_2: String = "theme_2"
    const val THEME_3: String = "theme_3"
    const val THEME_4: String = "theme_4"
    const val THEME_5: String = "theme_5"
    const val THEME_6: String = "theme_6"
    const val THEME_7: String = "theme_7"
    const val THEME_8: String = "theme_8"
    const val THEME_9: String = "theme_9"
    const val THEME_10: String = "theme_10"
    const val THEME_11: String = "theme_11"
    const val THEME_12: String = "theme_12"
    const val THEME_13: String = "theme_13"
    const val THEME_14: String = "theme_14"
    const val THEME_15: String = "theme_15"
    const val THEME_16: String = "theme_16"
    const val THEME_17: String = "theme_17"
    const val THEME_18: String = "theme_18"
    const val THEME_19: String = "theme_19"
    const val THEME_20: String = "theme_20"
    const val THEME_21: String = "theme_21"
    const val THEME_22: String = "theme_22"
    const val THEME_23: String = "theme_23"
    const val THEME_24: String = "theme_24"
    const val THEME_25: String = "theme_25"
    const val THEME_26: String = "theme_26"
    const val THEME_27: String = "theme_27"
    const val THEME_28: String = "theme_28"
    const val THEME_29: String = "theme_29"
    const val THEME_30: String = "theme_30"
    const val THEME_31: String = "theme_31"
    const val THEME_32: String = "theme_32"
    const val THEME_33: String = "theme_33"
    const val THEME_34: String = "theme_34"
    const val THEME_35: String = "theme_35"
    const val THEME_36: String = "theme_36"
    const val THEME_37: String = "theme_37"
    const val THEME_38: String = "theme_38"
    const val THEME_39: String = "theme_39"
    const val THEME_40: String = "theme_40"
    const val THEME_41: String = "theme_41"
    const val THEME_42: String = "theme_42"
    const val THEME_43: String = "theme_43"
    const val THEME_44: String = "theme_44"
    const val THEME_45: String = "theme_45"
    const val THEME_46: String = "theme_46"
    const val THEME_DEFAULT = "THEME_DEFAULT"


    const val COLOR_BG_DEFAULT: String = "#d2d5da"
    const val COLOR_TEXT_MAIN_DEFAULT: String = "#000000"
    const val COLOR_TEXT_SMALL_DEFAULT: String = "#008800"
    const val THEME_IOS_DEFAULT: String = "theme_ios_default"
    const val KEY_THEME: String = "key_theme"
    const val THEME_GREEN_MEDIUM: String = "theme_green_medium"
    const val BASE_URL_IMAGE: String = "http://207.148.116.90/app/"
    //them
    const val POSITIONFONT: String = "POSITIONFONT"
    const val FONTNAME: String = "FONTNAME"
    const val CATEGORYNAME: String = "CATEGORYNAME"
    const val CATEGORY_MY_BACKGROUND: String = "CATEGORY_MY_BACKGROUND"
    const val CATEGORY_API: String = "CATEGORY_API"
    const val CATEGORY: String = "CATEGORY"
    const val PATH_BACKGROUND_CROP: String = "PATH_BACKGROUND_CROP"
}