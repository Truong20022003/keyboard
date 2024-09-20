package com.sticker.nicekeyboard.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.preference.PreferenceManager
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.sticker.nicekeyboard.R
import com.sticker.nicekeyboard.base.BaseActivity
import com.sticker.nicekeyboard.custom.ObjectTheme
import com.sticker.nicekeyboard.custom.ShareTheme
import com.sticker.nicekeyboard.databinding.ActivityMainBinding
import com.sticker.nicekeyboard.databinding.DialogExitAppBinding
import com.sticker.nicekeyboard.rate.RatingDialog
import com.sticker.nicekeyboard.util.Constants
import com.sticker.nicekeyboard.util.Constants.CUSTOM_POSITION
import com.sticker.nicekeyboard.util.Constants.SETTING_POSITION
import com.sticker.nicekeyboard.util.Constants.THEME_POSITION
import com.sticker.nicekeyboard.util.Data
import com.sticker.nicekeyboard.util.tapAndCheckInternet
import java.util.Objects


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    private lateinit var viewPagerAdapter: BottomAdapter
    private var currentTab = THEME_POSITION

    override fun setBinding(layoutInflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): MainViewModel {
        return MainViewModel()
    }

    override fun initView() {
        bottom()
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        currentTab = THEME_POSITION
                        getDefaultFragment(
                            binding.cnBottom.ivTheme,
                            binding.cnBottom.tvTheme,
                            R.drawable.ic_theme_select
                        )
                    }

                    1 -> {
                        currentTab = CUSTOM_POSITION
                        getDefaultFragment(
                            binding.cnBottom.ivCustom,
                            binding.cnBottom.tvCustom,
                            R.drawable.ic_custom_select
                        )
                    }

                    2 -> {
                        currentTab = SETTING_POSITION
                        getDefaultFragment(
                            binding.cnBottom.ivSetting,
                            binding.cnBottom.tvSetting,
                            R.drawable.ic_setting_select
                        )
                    }
                }
            }
        })
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                dialogExitApp()
            }
        })
        Data.increaseCountOpenApp()
        val count = Data.getCountOpenApp()
        if (((count == 2) || (count == 4) || (count == 6)) && !Data.isRated()) {
            showRatingDialog()
        }
    }

    override fun bindView() {
        binding.cnBottom.linearTheme.tapAndCheckInternet {
            theme()
        }
        binding.cnBottom.linearCustom.tapAndCheckInternet {
            custom()
        }
        binding.cnBottom.linearSetting.tapAndCheckInternet {
            setting()
        }


        initializeKeyboard()
    }

    private fun bottom() {
        // view pager
        binding.viewPager.offscreenPageLimit = 1
        viewPagerAdapter = BottomAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.isUserInputEnabled = false
    }

    private fun getDefaultFragment(imageView: ImageView, textView: TextView, image: Int) {
        binding.cnBottom.ivTheme.setImageResource(R.drawable.ic_theme)
        binding.cnBottom.ivCustom.setImageResource(R.drawable.ic_custom)
        binding.cnBottom.ivSetting.setImageResource(R.drawable.ic_setting)
        binding.cnBottom.tvTheme.setTextColor(ContextCompat.getColor(this, R.color.color_9097A5))
        binding.cnBottom.tvCustom.setTextColor(ContextCompat.getColor(this, R.color.color_9097A5))
        binding.cnBottom.tvSetting.setTextColor(ContextCompat.getColor(this, R.color.color_9097A5))
        imageView.setImageResource(image)
        textView.setTextColor(ContextCompat.getColor(this, R.color.color_FA7A50))
    }

    private fun theme() {
        binding.viewPager.currentItem = THEME_POSITION
    }

    private fun custom() {
        binding.viewPager.currentItem = CUSTOM_POSITION
    }

    private fun setting() {
        binding.viewPager.currentItem = SETTING_POSITION
    }

    private fun initializeKeyboard() {
        val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val edit = defaultSharedPreferences.edit()
        if (defaultSharedPreferences.getBoolean("isFirstTime", true)) {
            val objectTheme: ObjectTheme = ShareTheme.getmIntance(this).getObjectTheme()
            objectTheme.backgroundKeyboard.setmStyleKeyboard(Constants.THEME_1)
            objectTheme.backgroundKeyboard.isBackground = false
            objectTheme.backgroundKeyboard.setIsAssets(false)
            objectTheme.backgroundKeyboard.setIsDrawable(true)
            objectTheme.backgroundKeyboard
                .setmPath_bg("style_background/basic_theme/style_bg125.png")
            ShareTheme.getmIntance(this).saveData(objectTheme)
            edit.putBoolean("isFirstTime", false)
            edit.apply()
        }
    }

    private fun dialogExitApp() {
        val bindingDialog = DialogExitAppBinding.inflate(layoutInflater)
        val dialog = Dialog(this)
        dialog.setContentView(bindingDialog.root)
        Objects.requireNonNull(dialog.window)?.setGravity(Gravity.CENTER)
        dialog.window!!.setLayout(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        bindingDialog.tvCancel.setOnClickListener {
            dialog.dismiss()
        }
        bindingDialog.tvQuit.setOnClickListener {
            dialog.dismiss()
            finishAffinity()
        }
        dialog.show()
    }
    private fun showRatingDialog() {
        val ratingDialog =
            RatingDialog(this@MainActivity)
        ratingDialog.setOnPress(object : RatingDialog.OnPress {

            override fun cancel() {

            }

            override fun later() {
                ratingDialog.dismiss()
            }

            override fun rating() {
                val manager: ReviewManager = ReviewManagerFactory.create(this@MainActivity)
                val request: Task<ReviewInfo> =
                    manager.requestReviewFlow()
                request.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val reviewInfo: ReviewInfo = task.result
                        val flow: Task<Void> =
                            manager.launchReviewFlow(this@MainActivity, reviewInfo)
                        flow.addOnSuccessListener {
                            Data.forceRated()
                            ratingDialog.dismiss()
                            dialogAfterRate()
                        }
                    } else {
                        ratingDialog.dismiss()
                    }
                }
            }

            override fun send(rate: Float) {
                Data.forceRated()
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.thank_you_for_rating_us),
                    Toast.LENGTH_SHORT
                ).show()

                ratingDialog.dismiss()
                dialogAfterRate()
            }
        })
        ratingDialog.show()
    }

    fun dialogAfterRate() {
        val builder = AlertDialog.Builder(this, R.style.full_screen_dialog)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.dialog_thank_you, null)
        builder.setView(dialogView)
        val dialog = builder.create()
        val tvGotIt = dialogView.findViewById<TextView>(R.id.tvGotIt)
        val imgClose = dialogView.findViewById<ImageView>(R.id.imgClose)
        tvGotIt.setOnClickListener { dialog.dismiss() }
        imgClose.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

}