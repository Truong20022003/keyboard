package com.sticker.nicekeyboard.ui.custom_background

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sticker.nicekeyboard.R
import com.sticker.nicekeyboard.call_api.CategoryWithImages
import com.sticker.nicekeyboard.call_api.ImageItem
import com.sticker.nicekeyboard.databinding.ItemBackgroundAdsBinding
import com.sticker.nicekeyboard.databinding.ItemBackgroundBinding
import com.sticker.nicekeyboard.remote.RemoteConfig
import com.sticker.nicekeyboard.sdklisten.manager.AdsNativeManager
import com.sticker.nicekeyboard.ui.custom_font.FontAdapter
import com.sticker.nicekeyboard.util.tapAndCheckInternet

class CategoryBackgroundAdapter(
    private val context: Context,
    private var categoriesWithImages: List<CategoryWithImages>,
    private var onClickAdapter: CategoryClickAdapter,
    private var onClickImageAdapter: ImageClickAdapter
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_TYPE_NORMAL = 1
        const val VIEW_TYPE_ADS = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (categoriesWithImages[position].isAds) {
            VIEW_TYPE_ADS
        } else {
            VIEW_TYPE_NORMAL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == FontAdapter.VIEW_TYPE_ADS) {
            val binding =
                ItemBackgroundAdsBinding.inflate(LayoutInflater.from(context), parent, false)
            AdsViewHolder(binding)
        } else {
            val binding = ItemBackgroundBinding.inflate(LayoutInflater.from(context), parent, false)
            CategoryViewHolder(binding)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(categoryList: List<CategoryWithImages>) {
        val updatedCategoryList = mutableListOf<CategoryWithImages>()
        categoryList.forEachIndexed { index, categoryList ->
            updatedCategoryList.add(categoryList)
            if (index == 0) {
                updatedCategoryList.add(CategoryWithImages("", emptyList(), false, true))
            }
        }
        categoriesWithImages = updatedCategoryList
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CategoryViewHolder) {
            val categoryWithImages = categoriesWithImages[position]
            holder.bind(categoryWithImages, position)
        } else if (holder is AdsViewHolder) {
            holder.bind()
        }
    }

    override fun getItemCount(): Int = categoriesWithImages.size

    inner class CategoryViewHolder(private val binding: ItemBackgroundBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(categoryWithImages: CategoryWithImages, position: Int) {
            binding.tvNameCategory.text = categoryWithImages.category

            binding.rcyImageCategory.layoutManager =
                GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
            binding.rcyImageCategory.adapter = ImageAdapter(
                context,
                categoryWithImages.images,
                object : ImageAdapter.ImageClickListener {
                    override fun onImageClick(imageItem: ImageItem) {
                        onClickImageAdapter.onImageClick(
                            categoryWithImages.category,
                            imageItem,
                            position
                        )
                    }

                })
            binding.tvSeeAll.tapAndCheckInternet {
                onClickAdapter.onCategoryClick(categoryWithImages.category)
            }
        }
    }

    inner class AdsViewHolder(val binding: ItemBackgroundAdsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isLoadAds = false

        fun bind() {
            if (isLoadAds) {
                return
            }
            AdsNativeManager.loadAdsNative1(
                binding.root,
                R.layout.ads_native_custom,
                RemoteConfig.native_custom,
                R.layout.ads_shimmer_native_custom
            )
            isLoadAds = true

        }
    }

    interface CategoryClickAdapter {
        fun onCategoryClick(categoryWithImages: String)
    }

    interface ImageClickAdapter {
        fun onImageClick(categoryWithImages: String, imageItem: ImageItem, position: Int)
    }
}
