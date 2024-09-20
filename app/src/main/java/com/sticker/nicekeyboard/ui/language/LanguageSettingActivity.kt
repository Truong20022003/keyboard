package com.sticker.nicekeyboard.ui.language


import android.content.Intent
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.sticker.nicekeyboard.base.BaseActivity
import com.sticker.nicekeyboard.databinding.ActivityLanguageSettingBinding
import com.sticker.nicekeyboard.ui.main.MainActivity
import com.sticker.nicekeyboard.util.SystemUtil
import com.sticker.nicekeyboard.util.tapAndCheckInternet


class LanguageSettingActivity : BaseActivity<ActivityLanguageSettingBinding, LanguageViewModel>() {


    private var codeLangUpdate = "en"
    private lateinit var languageAdapterUpdate: LanguageStartAdapter
    private var langDeviceUpdate = "en"

    override fun setBinding(layoutInflater: LayoutInflater): ActivityLanguageSettingBinding {
        return ActivityLanguageSettingBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): LanguageViewModel {
        return LanguageViewModel()
    }

    override fun initView() {
        val linearLayoutManager = LinearLayoutManager(this)
        languageAdapterUpdate =
            LanguageStartAdapter(
                this,
                mutableListOf(),
                object : LanguageStartAdapter.Listener {
                    override
                    fun onClickLanguage(languageModel: LanguageModel) {
                        viewModel.setSelectedLanguage(languageModel)
                        codeLangUpdate = languageModel.code
                    }
                })
        binding.rcyLanguage.layoutManager = linearLayoutManager
        binding.rcyLanguage.adapter = languageAdapterUpdate


        viewModel.languages.observe(this) { list ->
            languageAdapterUpdate.updateData(list)
        }

        viewModel.langDevice.observe(this) { langDevice ->
            this.langDeviceUpdate = langDevice
        }

        viewModel.codeLang.observe(this) { codeLang ->
            this.codeLangUpdate = codeLang
        }

        viewModel.first(this)

        viewModel.selectedLanguage.observe(this) { selectedLanguage ->
            languageAdapterUpdate.setSelectedLanguage(selectedLanguage)
        }
        binding.ivDoneLanguage.tapAndCheckInternet {
            SystemUtil.saveLocale(baseContext, codeLangUpdate)
            SystemUtil.setActive(baseContext, true)
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }
    }

    override fun bindView() {
        binding.back.tapAndCheckInternet {
            onBackPressedDispatcher.onBackPressed()
            finish()
        }
    }
}
