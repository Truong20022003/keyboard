package com.sticker.nicekeyboard.ui.intro

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.sticker.nicekeyboard.R

class IntroAdapter(var context: Context) : PagerAdapter() {

    private var layouts: IntArray = intArrayOf(
        R.layout.welcome_slide1,
        R.layout.welcome_slide2,
        R.layout.welcome_slide3,
        R.layout.welcome_slide4
    )
    private var inflater: LayoutInflater? = null

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater!!.inflate(layouts[position], container, false)
        container.addView(view)
        return view
    }

    override fun getCount(): Int {
        return layouts.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}