package com.sticker.nicekeyboard.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sticker.nicekeyboard.ui.fragment_custom.FragmentCustom
import com.sticker.nicekeyboard.ui.fragment_setting.FragmentSetting
import com.sticker.nicekeyboard.ui.fragment_theme.FragmentTheme


class BottomAdapter(fragmentManager: FragmentActivity) : FragmentStateAdapter(fragmentManager) {
    private val fragments = arrayOf(
        FragmentTheme(),
        FragmentCustom(),
        FragmentSetting()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun getFragment(position: Int): Fragment {
        return fragments[position]
    }
}