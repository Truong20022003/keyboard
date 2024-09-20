package com.sticker.nicekeyboard.ui.no_internet

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sticker.nicekeyboard.databinding.ActivityNoInternetBinding
import com.sticker.nicekeyboard.ui.splash.SplashActivity
import com.sticker.nicekeyboard.util.CheckInternet

class NoInternetActivity : AppCompatActivity() {
    private var strNoInterNet = ""

    private lateinit var binding: ActivityNoInternetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoInternetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.back.visibility = View.GONE
        if (intent.getStringExtra("BUNDLE_SPLASH_NO_INTERNET") != null) {
            strNoInterNet = intent.getStringExtra("BUNDLE_SPLASH_NO_INTERNET")!!
        }

        binding.tvRetry.setOnClickListener {
            if (CheckInternet.haveNetworkConnection(this)) {
                finish()
                overridePendingTransition(0, 0)
            } else {
                navigateToWifiSetting()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        if (CheckInternet.haveNetworkConnection(this)) {
            if (strNoInterNet != "") {
                val intent = Intent(this, SplashActivity::class.java)
                startActivity(intent)
                finish()
                overridePendingTransition(0, 0)
            } else {
                finish()
                overridePendingTransition(0, 0)
            }
        }
    }

    private fun navigateToWifiSetting() {
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        startActivity(intent)
    }
}