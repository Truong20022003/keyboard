package com.sticker.nicekeyboard.ui.custom_background

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sticker.nicekeyboard.call_api.ImageItem
import com.sticker.nicekeyboard.databinding.ItemImageBackgroundBinding

class MyBackgroundAdapter(
    private val context: Context,
    private var images: List<ImageItem>,
    private val myBackgroundClickAdapter: MyBackgroundClickAdapter
) : RecyclerView.Adapter<MyBackgroundAdapter.MyBackgroundViewHolder>() {

    companion object {
        private const val MAX_DISPLAY_COUNT = 6
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyBackgroundViewHolder {
        val binding =
            ItemImageBackgroundBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyBackgroundViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyBackgroundViewHolder, position: Int) {
        val imageItem = images[position]
        holder.bind(imageItem, position)
    }

    override fun getItemCount(): Int {
        return if (images.size > MAX_DISPLAY_COUNT) MAX_DISPLAY_COUNT else images.size
    }

    inner class MyBackgroundViewHolder(private val binding: ItemImageBackgroundBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageItem: ImageItem, position: Int) {
            if (position == 0) {
                binding.ivCamera.visibility = View.VISIBLE
                binding.itemImg.visibility = View.GONE
            } else {
                binding.ivCamera.visibility = View.GONE
                binding.itemImg.visibility = View.VISIBLE

                Glide.with(context)
                    .load(Uri.parse(imageItem.image)) // Sử dụng Uri
                    .into(binding.itemImg)
            }
            Log.d("aaa", imageItem.toString())
            if (position == MAX_DISPLAY_COUNT - 1 && images.size > MAX_DISPLAY_COUNT) {
                binding.tvNumber.visibility = View.VISIBLE
                binding.tvNumber.text = "+ ${images.size - MAX_DISPLAY_COUNT}"
                binding.viewShadow.visibility = View.VISIBLE
            } else {
                binding.tvNumber.visibility = View.GONE
                binding.viewShadow.visibility = View.GONE
            }
            binding.root.setOnClickListener {
                myBackgroundClickAdapter.onMyBackgroundClick(imageItem, position)
            }
        }
    }

    fun updateData(newThemeList: List<ImageItem>) {
        images = newThemeList
        notifyDataSetChanged()
    }

    interface MyBackgroundClickAdapter {
        fun onMyBackgroundClick(imageItem: ImageItem, position: Int)
    }
}
