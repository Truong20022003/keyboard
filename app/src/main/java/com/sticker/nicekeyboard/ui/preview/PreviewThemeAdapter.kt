package com.sticker.nicekeyboard.ui.preview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sticker.nicekeyboard.databinding.ItemPreviewBinding
import com.sticker.nicekeyboard.ui.fragment_theme.theme.ThemeModel


class PreviewThemeAdapter(
    private val context: Context,
    private var imageList: List<ThemeModel>
) : RecyclerView.Adapter<PreviewThemeAdapter.ImageViewHolder>() {
    fun updateData(newThemeList: List<ThemeModel>) {
        imageList = newThemeList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemPreviewBinding.inflate(LayoutInflater.from(context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(imageList[position].pathTheme)
    }

    override fun getItemCount(): Int = imageList.size

    inner class ImageViewHolder(private val binding: ItemPreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrl: String) {
            Glide.with(context).load(imageUrl).into(binding.ivKeyboard)
        }
    }
}