package com.sticker.nicekeyboard.ui.preview

import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.sticker.nicekeyboard.R
import com.sticker.nicekeyboard.base.BaseActivity
import com.sticker.nicekeyboard.custom.ObjectTheme
import com.sticker.nicekeyboard.custom.Prefrences
import com.sticker.nicekeyboard.custom.ShareTheme
import com.sticker.nicekeyboard.databinding.ActivityPreviewBinding
import com.sticker.nicekeyboard.remote.RemoteConfig
import com.sticker.nicekeyboard.sdklisten.manager.AdsBannerManager
import com.sticker.nicekeyboard.ui.fragment_theme.theme.ThemeModel
import com.sticker.nicekeyboard.ui.success.SuccessActivity
import com.sticker.nicekeyboard.util.Constants
import com.sticker.nicekeyboard.util.tapAndCheckInternet
import com.notebooksdk.notebookandroidsdk.NotebookAndroidSDK

import kotlin.math.abs

class PreviewActivity : BaseActivity<ActivityPreviewBinding, PreviewViewModel>() {
    private lateinit var previewThemeAdapter: PreviewThemeAdapter
    private var positionIntent: Int = 0
    private lateinit var objectTheme: ObjectTheme
    private var themeNamePager: String? = null
    private var listTheme: List<ThemeModel> = emptyList()
    private var pathBackground: String? = null

    override fun setBinding(layoutInflater: LayoutInflater): ActivityPreviewBinding {
        return ActivityPreviewBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): PreviewViewModel {
        return PreviewViewModel()
    }

    override fun initView() {


        NotebookAndroidSDK.notebookgetAdInstance().toggleActivity(this)
        AdsBannerManager.loadAdsBanner(this@PreviewActivity, RemoteConfig.collap_preview)
        if (RemoteConfig.getConfigBoolean(this,RemoteConfig.collap_preview)){
            binding.llBanner.visibility = View.VISIBLE
        }else{
            binding.llBanner.visibility = View.GONE
        }

        positionIntent = intent.getIntExtra(Constants.BUNDLE_POSITION, 0)
        val themeList: List<ThemeModel>? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableArrayListExtra(
                    Constants.BUNDLE_THEME_LIST,
                    ThemeModel::class.java
                )
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableArrayListExtra(Constants.BUNDLE_THEME_LIST)
            }
        if (themeList != null) {
            listTheme = themeList
            previewThemeAdapter = PreviewThemeAdapter(this, listTheme)
            binding.viewPagerTheme.adapter = previewThemeAdapter
            binding.viewPagerTheme.currentItem = positionIntent
            themeNamePager = listTheme[binding.viewPagerTheme.currentItem].themeName
            pathBackground = listTheme[binding.viewPagerTheme.currentItem].pathBackground
            Log.d("PreviewActivity", "Received Theme: $themeNamePager")
            setupViewPager()
        }

    }

    override fun bindView() {
        binding.ivHome.tapAndCheckInternet {
            onBackPressedDispatcher.onBackPressed()
            finish()
        }
        binding.tvApply.tapAndCheckInternet {
            if (!Constants.isMyKeyboardEnabled(this) || !Constants.isMyKeyboardActive(this) || !checkStoragePermission()) {
                dialogPermission()
            } else applyTheme()
        }
        binding.tvCancel.tapAndCheckInternet {
            onBackPressedDispatcher.onBackPressed()
            finish()
        }
    }


    private fun setupViewPager() {
        binding.viewPagerTheme.offscreenPageLimit = 1
        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx =
            resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx

        binding.viewPagerTheme.setPageTransformer { page, position ->
            page.translationX = -pageTranslationX * position
            page.scaleY = (1 - (0.25f * abs(position))).toFloat()
        }

        binding.viewPagerTheme.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                positionIntent = position
                themeNamePager = listTheme[position].themeName
                pathBackground = listTheme[position].pathBackground
                Log.d("aaa", "onPageSelected: name: $themeNamePager   pathBg:$pathBackground")
            }
        })
    }

    private fun applyTheme() {
        objectTheme = ShareTheme.getmIntance(this).getObjectTheme()
        val nameConstance: String = getThemeConstant(themeNamePager)
        objectTheme.backgroundKeyboard.setmStyleKeyboard(nameConstance)
        objectTheme.backgroundKeyboard.isBackground = false
        objectTheme.backgroundKeyboard.setIsAssets(false)
        objectTheme.backgroundKeyboard.setIsDrawable(true)
        objectTheme.backgroundKeyboard.setmPath_bg(pathBackground)
        ShareTheme.getmIntance(this).saveData(objectTheme)
        ShareTheme.getmIntance(this).putThemeNameConstance(nameConstance)
        Log.d("aaa", pathBackground.toString())
        startActivity(Intent(this, SuccessActivity::class.java))
    }

    private fun getThemeConstant(themeName: String?): String {
        return when (themeName) {
            Constants.THEME_1 -> Constants.THEME_1
            Constants.THEME_2 -> Constants.THEME_2
            Constants.THEME_3 -> Constants.THEME_3
            Constants.THEME_4 -> Constants.THEME_4
            Constants.THEME_5 -> Constants.THEME_5
            Constants.THEME_6 -> Constants.THEME_6
            Constants.THEME_7 -> Constants.THEME_7
            Constants.THEME_8 -> Constants.THEME_8
            Constants.THEME_9 -> Constants.THEME_9
            Constants.THEME_10 -> Constants.THEME_10
            Constants.THEME_11 -> Constants.THEME_11
            Constants.THEME_12 -> Constants.THEME_12
            Constants.THEME_13 -> Constants.THEME_13
            Constants.THEME_14 -> Constants.THEME_14
            Constants.THEME_15 -> Constants.THEME_15
            Constants.THEME_16 -> Constants.THEME_16
            Constants.THEME_17 -> Constants.THEME_17
            Constants.THEME_18 -> Constants.THEME_18
            Constants.THEME_19 -> Constants.THEME_19
            Constants.THEME_20 -> Constants.THEME_20
            Constants.THEME_21 -> Constants.THEME_21
            Constants.THEME_22 -> Constants.THEME_22
            Constants.THEME_23 -> Constants.THEME_23
            Constants.THEME_24 -> Constants.THEME_24
            Constants.THEME_25 -> Constants.THEME_25
            Constants.THEME_26 -> Constants.THEME_26
            Constants.THEME_27 -> Constants.THEME_27
            Constants.THEME_28 -> Constants.THEME_28
            Constants.THEME_29 -> Constants.THEME_29
            Constants.THEME_30 -> Constants.THEME_30
            Constants.THEME_31 -> Constants.THEME_31
            Constants.THEME_32 -> Constants.THEME_32
            Constants.THEME_33 -> Constants.THEME_33
            Constants.THEME_34 -> Constants.THEME_34
            Constants.THEME_35 -> Constants.THEME_35
            Constants.THEME_36 -> Constants.THEME_36
            Constants.THEME_37 -> Constants.THEME_37
            Constants.THEME_38 -> {
                setPreferencesForTheme(Constants.THEME_38, "rubik_one_regular.ttf", 0)
                Constants.THEME_38
            }

            Constants.THEME_39 -> {
                setPreferencesForTheme(Constants.THEME_39, "sedgwick_ave_display_regular.ttf", 1)
                Constants.THEME_39
            }

            Constants.THEME_40 -> {
                setPreferencesForTheme(Constants.THEME_40, "sigmar_one_regular.ttf", 2)
                Constants.THEME_40
            }

            Constants.THEME_41 -> {
                setPreferencesForTheme(Constants.THEME_41, "Roboto-Regular.ttf", 3)
                Constants.THEME_41
            }

            Constants.THEME_42 -> {
                setPreferencesForTheme(Constants.THEME_42, "nunito_semi_bold.ttf", 4)
                Constants.THEME_42
            }

            Constants.THEME_43 -> {
                setPreferencesForTheme(Constants.THEME_43, "nunito_semi_bold.ttf", 4)
                Constants.THEME_43
            }

            Constants.THEME_44 -> Constants.THEME_44
            Constants.THEME_45 -> Constants.THEME_45
            else -> Constants.THEME_15

        }

    }

    private fun setPreferencesForTheme(theme: String, font: String, fpos: Int) {
        Prefrences.setStringPref(this, "selectedFont", font)
        Prefrences.setPref(this, "fpos", fpos)
    }
}
