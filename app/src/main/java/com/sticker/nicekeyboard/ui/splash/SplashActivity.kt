package com.sticker.nicekeyboard.ui.splash

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.OnBackPressedCallback
import com.google.firebase.FirebaseApp
import com.notebooksdk.base.NotebookCallback
import com.notebooksdk.base.notebooksdkad.NotebookAdCallbackInfo
import com.notebooksdk.base.notebooksdkad.NotebookSDKInterstitialAdListener
import com.notebooksdk.notebookandroidsdk.NotebookAndroidSDK
import com.sticker.nicekeyboard.MyApplication
import com.sticker.nicekeyboard.base.BaseActivity
import com.sticker.nicekeyboard.databinding.ActivitySplashBinding
import com.sticker.nicekeyboard.remote.AdsSplash
import com.sticker.nicekeyboard.remote.RemoteConfig
import com.sticker.nicekeyboard.sdklisten.Constant
import com.sticker.nicekeyboard.sdklisten.dialog.LoadingAdsDialog
import com.sticker.nicekeyboard.sdklisten.listener.DemoAppOpenListener
import com.sticker.nicekeyboard.sdklisten.listener.DemoBannerListener
import com.sticker.nicekeyboard.sdklisten.listener.DemoCollapsibleBannerListener
import com.sticker.nicekeyboard.sdklisten.manager.AdsInterManager
import com.sticker.nicekeyboard.ui.language.LanguageStartActivity
import com.sticker.nicekeyboard.ui.no_internet.NoInternetActivity
import com.sticker.nicekeyboard.util.CheckInternet
import com.sticker.nicekeyboard.util.Constants


@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {

    // Sdk
    private var adsSplash: AdsSplash? = null
    var checkNoAds = false


    override fun setBinding(layoutInflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): SplashViewModel {
        return SplashViewModel()
    }

    override fun initView() {
        if (!isTaskRoot
            && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
            && intent.action != null && intent.action.equals(Intent.ACTION_MAIN)
        ) {
            finish()
            return
        }
        countDownTimer.start()
        // Sdk
        MyApplication.timeStart = System.currentTimeMillis()
        Constant.isShowAllAds = true
        Constant.timeFromStart = System.currentTimeMillis()
        firebase()
    }

    override fun bindView() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })
    }

    private val countDownTimer: CountDownTimer = object : CountDownTimer(3000, 5) {
        override fun onTick(millisUntilFinished: Long) {
            val progressValue = (((3000 - millisUntilFinished).toFloat() / 3000) * 100).toInt()
            binding.pb.progress = progressValue
            binding.txtProgress.text = "$progressValue%"
        }

        override fun onFinish() {
            binding.pb.progress = 100
            binding.txtProgress.text = "100%"
        }
    }


    override fun onResume() {
        super.onResume()
        MyApplication.offAppOpen()
    }

    private fun firebase() {
        FirebaseApp.initializeApp(baseContext)
        RemoteConfig.initRemoteConfig { task ->
            if (task.isSuccessful) {
                val update = task.result
                if (update == true) {
                    RemoteConfig.remoteConfig(this@SplashActivity)
                }
            }
            checkShowAds()
            setUpSDk()
        }
    }

    private fun checkShowAds() {
        adsSplash = AdsSplash.init(
            RemoteConfig.getConfigBoolean(baseContext, RemoteConfig.inter_splash),
            RemoteConfig.getConfigBoolean(baseContext, RemoteConfig.open_splash),
            RemoteConfig.getConfigString(baseContext, RemoteConfig.rate_aoa_inter_splash)
        )
    }

    private fun setUpSDk() {
        Thread {
            if (!com.sticker.nicekeyboard.remote.CheckInternet(this)
                    .haveNetworkConnection() || !Constant.isShowAllAds
            ) {
                Handler(Looper.getMainLooper()).postDelayed({
                    navigateToNextScreen()
                }, 5000)
            } else {
                if (RemoteConfig.getConfigBoolean(baseContext, RemoteConfig.appopen_resume)) {
                    MyApplication.onAppOpen()
                } else {
                    MyApplication.offAppOpen()
                }

                val sdk = NotebookCallback { isSuccess, result ->
                    Log.e("kh49", result)
                    if (result.equals(Constant.SDK_START)) {
                        runSDK(isSuccess)
                    } else if (result.equals(Constant.HAS_INIT)) {
                        showAdsHasInit()
                    } else if (result.equals(Constant.INITING)) {
                        setUpSDk()
                    } else {
                        navigateToNextScreen()
                    }
                }

                NotebookAndroidSDK.notebooksdkInit(this@SplashActivity, Constant.KEY, sdk)
            }
        }.start()

    }

    private fun showAdsHasInit() {
        bannerCallBack()
        when (adsSplash?.state) {
            AdsSplash.STATE.OPEN -> {
                showAdsAppOpen()
            }

            AdsSplash.STATE.INTER -> {
                showAdsInter()
            }

            else -> {
                navigateToNextScreen()
            }
        }
    }

    private fun showAdsAppOpen() {
        NotebookAndroidSDK.notebookgetAdInstance()
            .setNotebookSDKAppOpenAdListener(object : DemoAppOpenListener() {
                override fun onAppOpenAdClose(
                    unitId: String?,
                    callbackInfo: NotebookAdCallbackInfo?
                ) {
                    super.onAppOpenAdClose(unitId, callbackInfo)
                    navigateToNextScreen()
                }

                override fun onAppOpenAdTimeout(unitId: String?) {
                    super.onAppOpenAdTimeout(unitId)
                    navigateToNextScreen()
                }
            })

        showDialog(this@SplashActivity)
        MyApplication.offAppOpen()
        Handler(Looper.getMainLooper()).postDelayed({
            NotebookAndroidSDK.notebookgetAdInstance().showAppOpenWithTimeout(10.0F)
            dismissDialog()
        }, 2000)


    }

    private fun showAdsInter() {
        AdsInterManager.showInterSplash(this@SplashActivity, RemoteConfig.inter_splash) {
            navigateToNextScreen()
            Constant.timeBetweenInterval = 0L
        }
    }

    private fun runSDK(isSuccess: Boolean) {
        setCallBack()
        bannerCallBack()
        callBackCollapsibleBanner()
        NotebookAndroidSDK.notebookgetAttrInstance().setNotebookAttributionCallback {

        }
        if (isSuccess) {
            if (checkNoAds) {
                navigateToNextScreen()
            }
        } else {
            if (checkNoAds) {
                navigateToNextScreen()
            }
        }
    }


    private fun bannerCallBack() {
        NotebookAndroidSDK.notebookgetAdInstance()
            .setNotebookSDKBannerAdListener(DemoBannerListener())
    }

    private fun callBackCollapsibleBanner() {
        NotebookAndroidSDK.notebookgetAdInstance().setNotebookSDKCollapsibleBannerAdListener(
            DemoCollapsibleBannerListener()
        )
    }

    private fun setCallBack() {
        when (adsSplash?.state) {
            AdsSplash.STATE.OPEN -> {
                callBackAppOpen()
            }

            AdsSplash.STATE.INTER -> {
                callBackInter()
            }

            else -> {
                checkNoAds = true
            }
        }
    }

    private fun callBackAppOpen() {
        NotebookAndroidSDK.notebookgetAdInstance()
            .setNotebookSDKAppOpenAdListener(object : DemoAppOpenListener() {
                override fun onAppOpenAdClose(
                    unitId: String?,
                    callbackInfo: NotebookAdCallbackInfo?
                ) {
                    super.onAppOpenAdClose(unitId, callbackInfo)
                    navigateToNextScreen()
                }

                override fun onAppOpenAdTimeout(unitId: String?) {
                    super.onAppOpenAdTimeout(unitId)
                    navigateToNextScreen()
                }
            })

        NotebookAndroidSDK.notebookgetAdInstance().setNotebookSDKAdvCompletedListener {
            showDialog(this@SplashActivity)
            MyApplication.offAppOpen()
            Handler(Looper.getMainLooper()).postDelayed({
                NotebookAndroidSDK.notebookgetAdInstance().showAppOpenWithTimeout(10.0F)
                dismissDialog()
            }, 1000)
        }


    }


    private fun callBackInter() {
        NotebookAndroidSDK.notebookgetAdInstance().setNotebookSDKInterstitialAdListener(object :
            NotebookSDKInterstitialAdListener {
            override fun onInterstitialAdShow(
                unitId: String?,
                callbackInfo: NotebookAdCallbackInfo?
            ) {

            }

            override fun onInterstitialAdClose(
                unitId: String?,
                callbackInfo: NotebookAdCallbackInfo?
            ) {

            }

            override fun onInterstitialAdClick(
                unitId: String?,
                callbackInfo: NotebookAdCallbackInfo?
            ) {

            }

            override fun onInterstitialAdLoaded(
                unitId: String?,
                callbackInfo: NotebookAdCallbackInfo?
            ) {
                AdsInterManager.showInterSplash(
                    this@SplashActivity,
                    RemoteConfig.inter_splash
                ) {
                    navigateToNextScreen()
                    Constant.timeBetweenInterval = 0L
                }

            }

            override fun onInterstitialAdLoadFailed(
                unitId: String?,
                errorCode: Int,
                errorMsg: String?
            ) {
                navigateToNextScreen()
                Constant.timeBetweenInterval = 0L
            }

            override fun onInterstitialAdPlayFail(p0: String?, p1: Int, p2: String?) {
                navigateToNextScreen()
                Constant.timeBetweenInterval = 0L
            }

        })
    }

    var dialog: LoadingAdsDialog? = null
    private fun showDialog(context: Activity) {
        try {
            try {
                runOnUiThread {
                    dialog =
                        LoadingAdsDialog(
                            this@SplashActivity
                        )
                    dialog?.show()
                }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun dismissDialog() {
        try {
            Handler(Looper.getMainLooper()).postDelayed({
                try {
                    dialog = if ((dialog != null
                                && dialog?.window != null
                                && dialog?.window?.decorView?.isAttachedToWindow == true)
                        && !this@SplashActivity.isDestroyed
                        && dialog?.isShowing == true
                    ) {
                        dialog?.dismiss()
                        null
                    } else {
                        null
                    }
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                }
            }, 2000)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

//    private fun initUserPayment() {
//        val userPaymentInterface = NotebookAndroidSDK.notebookgetUserPaymentInterface()
//        userPaymentInterface.autoLoginAsync(false, object :
//            NotebookSDKCallback<NotebookAutoLoginResult> {
//            override fun success(p0: NotebookAutoLoginResult?) {
//                if (checkNoAds) {
//                    navigateToNextScreen()
//                }
//            }
//
//            override fun fail(p0: NotebookState?) {
//                if (checkNoAds) {
//                    navigateToNextScreen()
//                }
//            }
//
//        })
//    }

    private fun navigateToNextScreen() {
        if (!CheckInternet.haveNetworkConnection(this)) {
            val intent = Intent(this, NoInternetActivity::class.java)
            intent.putExtra(Constants.BUNDLE_SPLASH_NO_INTERNET, Constants.BUNDLE_SPLASH)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        } else {
            val intent = Intent(this@SplashActivity, LanguageStartActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}