package com.sticker.nicekeyboard

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.notebooksdk.base.notebooksdkad.NotebookAdCallbackInfo
import com.notebooksdk.notebookandroidsdk.NotebookAndroidSDK
import com.sticker.nicekeyboard.sdklisten.dialog.LoadingAdsDialog
import com.sticker.nicekeyboard.sdklisten.listener.DemoAppOpenListener
import com.sticker.nicekeyboard.sdklisten.manager.AdsManager
import com.sticker.nicekeyboard.ui.splash.SplashActivity
import com.sticker.nicekeyboard.util.Data



class MyApplication : Application(), DefaultLifecycleObserver {
    var isStartApp = false
    var currentActivity: Activity? = null

    companion object {
        var timeStart = 0L
        var isShowResume = true
        fun offAppOpen() {
            isShowResume = false
        }

        fun onAppOpen() {
            isShowResume = true
        }

        @JvmStatic
        fun startOnce(application: Application) {
        }
    }

    override fun onCreate() {
        Data.initInstance(applicationContext)
        super<Application>.onCreate()
        startOnce(this)
        isStartApp = true
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                if (isStartApp) {
                    if (activity.javaClass.name != SplashActivity::class.java.name) {
                        activity.startActivity(Intent(activity, SplashActivity::class.java))
                        activity.finishAffinity()
                        return
                    }
                }
                isStartApp = false
            }

            override fun onActivityStarted(activity: Activity) {
                currentActivity = activity
            }

            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }

    private fun showAdsAppOpen() {
        NotebookAndroidSDK.notebookgetAdInstance()
            .setNotebookSDKAppOpenAdListener(object : DemoAppOpenListener() {
                override fun onAppOpenAdShow(s: String?, notebookAdCallbackInfo: NotebookAdCallbackInfo?) {
                    super.onAppOpenAdShow(s, notebookAdCallbackInfo)
                    offAppOpen()
                }

            })
        NotebookAndroidSDK.notebookgetAdInstance().showAppOpenWithTimeout(10.0F)
        offAppOpen()
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        try {
            currentActivity?.let {
                if (currentActivity?.javaClass != SplashActivity::class.java
                    && isShowResume
                ) {
                    offAppOpen()
                    showDialog(it)
                    AdsManager.setAdsShow(true)
                    Handler(Looper.getMainLooper()).postDelayed({
                        showAdsAppOpen()
                        dismissDialog()
                    }, 1000)

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }


    var dialog: LoadingAdsDialog? = null
    private fun showDialog(activity: Activity) {
        try {
            try {
                activity.runOnUiThread {
                    dialog =
                        LoadingAdsDialog(
                            activity
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
                    dialog = if (dialog != null
                        && dialog?.window != null
                        && dialog?.window?.decorView?.isAttachedToWindow == true
                        && currentActivity != null
                        && currentActivity?.isDestroyed == false
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
}