package com.sticker.nicekeyboard.util

import android.content.Context
import java.util.Locale

object SystemUtil {
    private var myLocale: Locale? = null

    // Lưu ngôn ngữ đã cài đặt
    fun saveLocale(context: Context, lang: String?) {
        setPreLanguage(context, lang)
    }

    // Load lại ngôn ngữ đã lưu và thay đổi chúng
    fun setLocale(context: Context): Context {
        val language = getPreLanguage(context)
        return if (language == "") {
            val config = context.resources.configuration
            val locale = Locale.getDefault()
            Locale.setDefault(locale)
            config.setLocale(locale)
            context.createConfigurationContext(config)
        } else {
            changeLang(language, context)
        }
    }

    // method phục vụ cho việc thay đổi ngôn ngữ.
    private fun changeLang(lang: String?, context: Context): Context {
        if (lang.equals("", ignoreCase = true)) return context
        myLocale = Locale(lang)
        saveLocale(context, lang)
        Locale.setDefault(myLocale)
        val config = context.resources.configuration
        config.setLocale(myLocale)
        return context.createConfigurationContext(config)
    }

    fun getPreLanguage(mContext: Context): String? {
        val preferences = mContext.getSharedPreferences("data", Context.MODE_PRIVATE)
        return preferences.getString("KEY_LANGUAGE", "")
    }

    private fun setPreLanguage(context: Context, language: String?) {
        if (language == null || language == "") {
        } else {
            val preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString("KEY_LANGUAGE", language)
            editor.apply()
        }
    }

    fun setActive(context: Context, value: Boolean) {
        val sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("active", value)
        editor.apply()
    }

    fun getActive(context: Context, value: Boolean): Boolean {
        val sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("active", value)
    }

}
