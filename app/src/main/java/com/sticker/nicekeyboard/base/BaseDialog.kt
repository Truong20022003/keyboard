package com.sticker.nicekeyboard.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.sticker.nicekeyboard.R

abstract class BaseDialog<T : ViewBinding>(context: Context) :
    Dialog(context, R.style.full_screen_dialog) {
    lateinit var binding: T

    private fun getInflatedLayoutNew(inflater: LayoutInflater): View {
        binding = setBindingNew(inflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getInflatedLayoutNew(layoutInflater))
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    abstract fun setBindingNew(layoutInflater: LayoutInflater): T

}