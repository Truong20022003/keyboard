package com.sticker.nicekeyboard.ui.fragment_setting

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.sticker.nicekeyboard.MyApplication
import com.sticker.nicekeyboard.R

class FragmentSettingViewModel : ViewModel() {
    fun share(context: Context) {
        MyApplication.offAppOpen()
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "https://play.google.com/store/apps/details?id=" + context.packageName
        )
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)))
    }
}