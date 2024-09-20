package com.sticker.nicekeyboard.ui.success

import android.content.Intent
import android.view.LayoutInflater
import com.sticker.nicekeyboard.base.BaseActivity

import com.sticker.nicekeyboard.databinding.ActivitySuccessBinding
import com.sticker.nicekeyboard.remote.RemoteConfig
import com.sticker.nicekeyboard.sdklisten.manager.AdsInterManager
import com.sticker.nicekeyboard.ui.main.MainActivity
import com.sticker.nicekeyboard.util.tapAndCheckInternet




class SuccessActivity : BaseActivity<ActivitySuccessBinding, SuccessViewModel>() {
    override fun setBinding(layoutInflater: LayoutInflater): ActivitySuccessBinding {
        return ActivitySuccessBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): SuccessViewModel {
        return SuccessViewModel()
    }

    override fun initView() {
        binding.ivHome.tapAndCheckInternet {
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }
        binding.tvExploreMore.tapAndCheckInternet {
            AdsInterManager.showInter(this@SuccessActivity, RemoteConfig.inter_success) {
                onBackPressedDispatcher.onBackPressed()
                finish()
            }
        }
    }

    override fun bindView() {

    }

}