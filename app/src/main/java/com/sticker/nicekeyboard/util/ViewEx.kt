package com.sticker.nicekeyboard.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.view.View
import com.sticker.nicekeyboard.ui.no_internet.NoInternetActivity


fun View.tapAndCheckInternet(action: (view: View?) -> Unit) {
    // Khởi tạo biến để theo dõi thời gian của lần click cuối cùng.
    var lastClickTime: Long = 0

    setOnClickListener {
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastClickTime >= 500) {
            lastClickTime = currentTime

            if (!CheckInternet.haveNetworkConnection(context)) {
                context.findActivity()?.let {
                    val intent = Intent(it, NoInternetActivity::class.java)
                    it.startActivity(intent)
                    it.overridePendingTransition(0, 0)
                }
            } else {
                action(this)
            }
        }
    }
}

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}
