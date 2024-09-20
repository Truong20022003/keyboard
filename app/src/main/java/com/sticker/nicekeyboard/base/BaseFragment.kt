package com.sticker.nicekeyboard.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T : ViewBinding, V : ViewModel> : Fragment() {
    lateinit var viewModel: V
    lateinit var binding: T

    private lateinit var activity: Activity

    abstract fun setViewModel(): V
    abstract fun getViewBinding(): T
    abstract fun initView()
    abstract fun bindView()
    override fun getContext(): Activity {
        return activity
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBinding()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = setViewModel()
        initView()
        bindView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as Activity
    }
    protected fun toast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}