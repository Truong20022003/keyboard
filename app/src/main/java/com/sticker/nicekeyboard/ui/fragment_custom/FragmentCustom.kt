package com.sticker.nicekeyboard.ui.fragment_custom

import android.content.Intent
import com.sticker.nicekeyboard.R
import com.sticker.nicekeyboard.base.BaseFragment
import com.sticker.nicekeyboard.databinding.FragmentCustomBinding
import com.sticker.nicekeyboard.ui.custom_background.BackgroundActivity
import com.sticker.nicekeyboard.ui.custom_font.FontActivity
import com.sticker.nicekeyboard.util.Constants
import com.sticker.nicekeyboard.util.SPUtils
import com.sticker.nicekeyboard.util.tapAndCheckInternet


class FragmentCustom : BaseFragment<FragmentCustomBinding, FragmentCustomViewModel>() {
    override fun setViewModel(): FragmentCustomViewModel {
        return FragmentCustomViewModel()
    }

    override fun getViewBinding(): FragmentCustomBinding {
        return FragmentCustomBinding.inflate(layoutInflater)
    }

    override fun initView() {
        binding.cvFont.tapAndCheckInternet {
            startActivity(Intent(requireContext(), FontActivity::class.java))
        }
        binding.cvBackground.tapAndCheckInternet {
            startActivity(Intent(requireContext(), BackgroundActivity::class.java))
        }
    }

    override fun bindView() {

    }

    override fun onResume() {
        super.onResume()
        binding.tvDefaultFont.text = SPUtils.getString(
            requireContext(), Constants.FONTNAME, getString(
                R.string.Default
            )
        )
    }
}