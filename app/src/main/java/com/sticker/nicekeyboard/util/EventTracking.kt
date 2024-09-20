package com.sticker.nicekeyboard.util

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics


object EventTracking {

    fun logEvent(context: Context, nameEvent: String) {
        val bundle = Bundle()
        FirebaseAnalytics.getInstance(context).logEvent(nameEvent, bundle)
    }

    fun logEvent(context: Context, nameEvent: String, param: String, value: String) {
        val bundle = Bundle()
        bundle.putString(param, value)
        FirebaseAnalytics.getInstance(context).logEvent(nameEvent, bundle)
    }

    fun logEvent(context: Context, nameEvent: String, param: String, value: Long) {
        val bundle = Bundle()
        bundle.putLong(param, value)
        FirebaseAnalytics.getInstance(context).logEvent(nameEvent, bundle)
    }

}