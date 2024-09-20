package com.sticker.nicekeyboard.ui.custom_font

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sticker.nicekeyboard.R
import com.sticker.nicekeyboard.base.BaseActivity
import com.sticker.nicekeyboard.custom.Prefrences
import com.sticker.nicekeyboard.databinding.ActivityFontBinding
import com.sticker.nicekeyboard.ui.success.SuccessActivity
import com.sticker.nicekeyboard.util.Constants
import com.sticker.nicekeyboard.util.SPUtils
import com.sticker.nicekeyboard.util.tapAndCheckInternet

class FontActivity : BaseActivity<ActivityFontBinding, FontViewModel>() {
    private lateinit var fontAdapter: FontAdapter
    private var positionSelected: Int? = null
    override fun setBinding(layoutInflater: LayoutInflater): ActivityFontBinding {
        return ActivityFontBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): FontViewModel {
        return FontViewModel()
    }

    override fun initView() {

        getFont()
    }

    override fun bindView() {
        binding.toolbar.tvToolbar.text = getString(R.string.font)
        binding.toolbar.back.tapAndCheckInternet {
            onBackPressedDispatcher.onBackPressed()
            finish()
        }
    }

    private fun getFont() {
        positionSelected = SPUtils.getInt(this, Constants.POSITIONFONT, 0)
        Log.d("aaa", Thread.currentThread().name + "main_______")
        viewModel.loadThemes(this, "fonts")
        fontAdapter = FontAdapter(this, emptyList(), object : FontAdapter.FontClickListener {
            override fun onCategoryClick(fontModel: FontModel, position: Int) {

                if (!Constants.isMyKeyboardEnabled(this@FontActivity) || !Constants.isMyKeyboardActive(
                        this@FontActivity
                    ) || !checkStoragePermission()
                ) {
                    dialogPermission()
                } else {
                    val previousPosition = fontAdapter.selectPosition
                    fontAdapter.selectPosition = position
                    fontAdapter.notifyItemChanged(previousPosition)
                    fontAdapter.notifyItemChanged(fontAdapter.selectPosition)
                    SPUtils.setInt(this@FontActivity, Constants.POSITIONFONT, position)
                    SPUtils.setString(this@FontActivity, Constants.FONTNAME, fontModel.nameFont)
                    val fontPath = fontModel.pathFont.removePrefix("file:///android_asset/fonts/")
                    Prefrences.setStringPref(this@FontActivity, "selectedFont", fontPath)
                    Prefrences.setPref(this@FontActivity, "fpos", position)
                    startActivity(Intent(this@FontActivity, SuccessActivity::class.java))

                }
            }

        }, positionSelected!!)


        binding.rcyFont.apply {
            val layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
            smoothScrollToPosition(positionSelected!!)
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (fontAdapter.getItemViewType(position) == FontAdapter.VIEW_TYPE_ADS) {
                        2
                    } else {
                        1
                    }
                }
            }
            binding.rcyFont.layoutManager = layoutManager
            adapter = fontAdapter
        }

        viewModel.fontList.observe(this) {
            fontAdapter.updateData(it)
        }
    }
}