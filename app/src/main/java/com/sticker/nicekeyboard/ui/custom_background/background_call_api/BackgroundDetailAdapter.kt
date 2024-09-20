package com.sticker.nicekeyboard.ui.custom_background.background_call_api

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sticker.nicekeyboard.call_api.ImageItem
import com.sticker.nicekeyboard.databinding.ItemImageBackgroundBinding
import com.sticker.nicekeyboard.util.Constants
import com.sticker.nicekeyboard.util.tapAndCheckInternet

class BackgroundDetailAdapter(
    private val context: Context,
    private var images: List<ImageItem>,
    private val backgroundDetailClick: BackgroundDetailClick
) : RecyclerView.Adapter<BackgroundDetailAdapter.ImageViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding =
            ItemImageBackgroundBinding.inflate(LayoutInflater.from(context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageItem = images[position]
        holder.bind(imageItem, position)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class ImageViewHolder(private val binding: ItemImageBackgroundBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageItem: ImageItem, position: Int) {
            Glide.with(context).load(Constants.BASE_URL_IMAGE + imageItem.image)
                .into(binding.itemImg)
            binding.root.tapAndCheckInternet {
                backgroundDetailClick.onBackgroundDetailClick(imageItem, position)
            }
        }
    }

    fun updateData(imageItem: List<ImageItem>) {
        images = imageItem
        notifyDataSetChanged()
    }

    interface BackgroundDetailClick {
        fun onBackgroundDetailClick(imageItem: ImageItem, position: Int)
    }

}
//theem