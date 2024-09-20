package com.sticker.nicekeyboard.ui.fragment_theme.theme

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sticker.nicekeyboard.databinding.ItemThemeHomeBinding

class ThemeAdapter(
    private val context: Context,
    private var themeList: List<ThemeModel>,
    private val themeClick: ThemeClick
) : RecyclerView.Adapter<ThemeAdapter.ThemeViewHolder>() {

    fun updateData(newThemeList: List<ThemeModel>) {
        themeList = newThemeList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeViewHolder {
        val binding = ItemThemeHomeBinding.inflate(LayoutInflater.from(context), parent, false)
        return ThemeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ThemeViewHolder, position: Int) {
        val theme = themeList[position]
        holder.bind(theme, position)
    }

    override fun getItemCount(): Int = themeList.size

    inner class ThemeViewHolder(private val binding: ItemThemeHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(themeModel: ThemeModel, position: Int) {
            Glide.with(context).load(themeModel.pathTheme).into(binding.ivTheme)
            binding.ivTheme.setOnClickListener {
                themeClick.onClickItem(themeModel, position)
            }
        }
    }

    interface ThemeClick {
        fun onClickItem(themeModel: ThemeModel, position: Int)
    }
}