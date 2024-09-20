package com.sticker.nicekeyboard.ui.custom_background

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sticker.nicekeyboard.call_api.ImageItem
import com.sticker.nicekeyboard.databinding.ItemImageBackgroundBinding
import com.sticker.nicekeyboard.ui.custom_background.background_call_api.BackgroundDetailActivity
import com.sticker.nicekeyboard.util.Constants
import com.sticker.nicekeyboard.util.tapAndCheckInternet


class ImageAdapter(
    private val context: Context,
    private var images: List<ImageItem>,
    private val imageClickListener: ImageClickListener
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    companion object {
        private const val MAX_DISPLAY_COUNT = 6
    }

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
        return if (images.size > MAX_DISPLAY_COUNT) MAX_DISPLAY_COUNT else images.size
    }

    inner class ImageViewHolder(private val binding: ItemImageBackgroundBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageItem: ImageItem, position: Int) {
            Glide.with(context).load(Constants.BASE_URL_IMAGE + imageItem.image)
                .into(binding.itemImg)
            if (position == MAX_DISPLAY_COUNT - 1 && images.size > MAX_DISPLAY_COUNT) {
                binding.tvNumber.visibility = View.VISIBLE
                binding.tvNumber.text = "+ ${images.size - MAX_DISPLAY_COUNT}"
                binding.viewShadow.visibility = View.VISIBLE
            } else {
                binding.tvNumber.visibility = View.GONE
                binding.viewShadow.visibility = View.GONE
            }
            binding.root.tapAndCheckInternet {
                if (position == 5) {
                    val intent =
                        Intent(context, BackgroundDetailActivity::class.java)
                    intent.putExtra(Constants.CATEGORYNAME, imageItem.category)
                    intent.putExtra(Constants.CATEGORY, Constants.CATEGORY_API)
                    context.startActivity(intent)
                } else {
                    imageClickListener.onImageClick(imageItem)
                }
            }
        }
    }

    fun updateData(newThemeList: List<ImageItem>) {
        images = newThemeList
        notifyDataSetChanged()
    }

    interface ImageClickListener {
        fun onImageClick(imageItem: ImageItem)
    }
}
