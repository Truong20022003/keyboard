package com.sticker.nicekeyboard.ui.policy

import android.annotation.SuppressLint
import android.view.LayoutInflater
import com.sticker.nicekeyboard.base.BaseActivity
import com.sticker.nicekeyboard.databinding.ActivityPolicyBinding
import com.sticker.nicekeyboard.util.tapAndCheckInternet

class PolicyActivity : BaseActivity<ActivityPolicyBinding, PolicyViewModel>() {
    override fun setBinding(layoutInflater: LayoutInflater): ActivityPolicyBinding {
        return ActivityPolicyBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): PolicyViewModel {
        return PolicyViewModel()
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
        binding.toolbar.back.tapAndCheckInternet {
            onBackPressedDispatcher.onBackPressed()
            finish()
        }
        binding.webView.loadUrl("https://www.julyend.cc/")
        binding.webView.settings.javaScriptEnabled = true
    }

    override fun bindView() {

    }

}