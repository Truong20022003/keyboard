package com.sticker.nicekeyboard.ui.language

import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sticker.nicekeyboard.R
import com.sticker.nicekeyboard.util.SystemUtil

import java.util.Locale

class LanguageViewModel : ViewModel() {

    val languages = MutableLiveData<List<LanguageModel>>()
    val selectedLanguage = MutableLiveData<LanguageModel>()

    private val _langDevice = MutableLiveData<String>()
    val langDevice: LiveData<String> get() = _langDevice

    private val _codeLang = MutableLiveData<String>()
    val codeLang: LiveData<String> get() = _codeLang

    fun first(context: Context) {
        val listLanguage = mutableListOf(
            LanguageModel("English", "en", false, R.drawable.language_en),
            LanguageModel("Hindi", "hi", false, R.drawable.language_hi),
            LanguageModel("Spanish", "es", false, R.drawable.language_es),
            LanguageModel("French", "fr", false, R.drawable.language_fr),
            LanguageModel("Portuguese", "pt", false, R.drawable.language_pt),
            LanguageModel("Indonesian", "in", false, R.drawable.language_in),
            LanguageModel("German", "de", false, R.drawable.language_de),
            LanguageModel("Japan", "ja", false, R.drawable.language_japan)
        )


        var langDevice = "en"
        var codeLang = "en"

        var position = 0
        var isLangDefault = false
        if (SystemUtil.getActive(context, false)) {
            val locale: Locale =
                Resources.getSystem().configuration.locales[0]

            langDevice = locale.language


            if (position > 0 && isLangDefault) {
                val languageModel = listLanguage[position]
                listLanguage.removeAt(position)
                listLanguage.add(0, languageModel)
            }


            var check = -1
            for (i in listLanguage.indices) {
                val languageModel = listLanguage[i]
                if (languageModel.code.contains(SystemUtil.getPreLanguage(context).toString())) {
                    listLanguage[i].active = true
                    listLanguage.remove(languageModel)
                    listLanguage.add(0, languageModel)
                    check = i
                    break
                }
            }
            listLanguage[0].active = false
            codeLang = listLanguage[0].code
        } else {
            val locale: Locale =
                Resources.getSystem().configuration.locales[0]

            langDevice = locale.language

            for ((index, languageModel) in listLanguage.withIndex()) {
                if (languageModel.code.contains(langDevice.lowercase(Locale.getDefault()))) {
                    isLangDefault = true
                    position = index
                    break
                }
            }

            if (position > 0 && isLangDefault) {
                val languageModel = listLanguage[position]
                listLanguage.removeAt(position)
                listLanguage.add(0, languageModel)
            }

            listLanguage[0].active = false
            codeLang = listLanguage[0].code

        }
        _langDevice.postValue(langDevice)
        _codeLang.postValue(codeLang)
        languages.postValue(listLanguage)
    }


    fun setSelectedLanguage(language: LanguageModel) {
        selectedLanguage.value = language
        _codeLang.postValue(language.code)
    }
}
