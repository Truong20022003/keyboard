package com.sticker.nicekeyboard.ui.intro

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.sticker.nicekeyboard.base.BaseActivity
import com.sticker.nicekeyboard.databinding.ActivityIntroBinding
import com.sticker.nicekeyboard.remote.RemoteConfig
import com.sticker.nicekeyboard.sdklisten.manager.AdsInterManager
import com.sticker.nicekeyboard.ui.main.MainActivity
import com.sticker.nicekeyboard.ui.permission.PermissionActivity
import com.sticker.nicekeyboard.util.Constants
import com.sticker.nicekeyboard.util.tapAndCheckInternet

class IntroActivity : BaseActivity<ActivityIntroBinding, IntroViewModel>() {

    override fun setBinding(layoutInflater: LayoutInflater): ActivityIntroBinding {
        return ActivityIntroBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): IntroViewModel {
        return IntroViewModel()
    }

    override fun initView() {
        val adapter = IntroAdapter(this)
        binding.viewPager.adapter = adapter
        binding.indicator.attachTo(binding.viewPager)
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        if (RemoteConfig.getConfigBoolean(
                                this@IntroActivity,
                                RemoteConfig.native_intro
                            )
                        ) {
                            binding.rootAds.rootAds.visibility = View.VISIBLE
                        } else {
                            binding.rootAds.rootAds.visibility = View.GONE
                        }
                    }

                    1 -> {
                        binding.rootAds.rootAds.visibility = View.GONE
                    }

                    2 -> {
                        binding.rootAds.rootAds.visibility = View.GONE
                    }

                    3 -> {
                        if (RemoteConfig.getConfigBoolean(
                                this@IntroActivity,
                                RemoteConfig.native_intro
                            )
                        ) {
                            binding.rootAds.rootAds.visibility = View.VISIBLE
                        } else {
                            binding.rootAds.rootAds.visibility = View.GONE
                        }
                    }
                }

            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
        binding.btnNext.tapAndCheckInternet {
            if (binding.viewPager.currentItem < 3) {
                binding.viewPager.currentItem += 1
            } else {
                AdsInterManager.showInter(this@IntroActivity, RemoteConfig.inter_intro) {
                    showActivity()
                }

            }
        }
    }

    override fun bindView() {

    }

    private fun showActivity() {
        if (!Constants.isMyKeyboardEnabled(this@IntroActivity) || !Constants.isMyKeyboardActive(
                this@IntroActivity
            ) || !checkStoragePermission()
        ) {
            startActivity(
                Intent(
                    this@IntroActivity,
                    PermissionActivity::class.java
                )
            )
        } else {
            startActivity(
                Intent(
                    this@IntroActivity,
                    MainActivity::class.java
                )
            )

        }
        finishAffinity()
    }

}