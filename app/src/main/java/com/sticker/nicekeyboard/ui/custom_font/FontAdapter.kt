package com.sticker.nicekeyboard.ui.custom_font

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sticker.nicekeyboard.R
import com.sticker.nicekeyboard.databinding.ItemFontAdsBinding
import com.sticker.nicekeyboard.databinding.ItemFontBinding
import com.sticker.nicekeyboard.remote.RemoteConfig
import com.sticker.nicekeyboard.sdklisten.manager.AdsNativeManager
import com.sticker.nicekeyboard.util.tapAndCheckInternet

class FontAdapter(
    private val context: Context,
    private var fontList: List<FontModel>,
    private val fontClick: FontClickListener,
    var selectPosition: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_NORMAL = 1
        const val VIEW_TYPE_ADS = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (fontList[position].isAds) {
            VIEW_TYPE_ADS
        } else {
            VIEW_TYPE_NORMAL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ADS) {
            val binding = ItemFontAdsBinding.inflate(LayoutInflater.from(context), parent, false)
            AdsViewHolder(binding)
        } else {
            val binding = ItemFontBinding.inflate(LayoutInflater.from(context), parent, false)
            FontViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FontViewHolder) {
            val fontModel = fontList[position]
            holder.bind(fontModel, position)
        } else if (holder is AdsViewHolder) {
            val fontModel = fontList[position]
            holder.bind(fontModel, position)
        }


    }

    override fun getItemCount(): Int = fontList.size

    inner class FontViewHolder(private val binding: ItemFontBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(fontModel: FontModel, position: Int) {
            binding.tvFontName.text = fontModel.nameFont
            val fontPath = fontModel.pathFont.removePrefix("file:///android_asset/")
            try {
                val typeface = Typeface.createFromAsset(context.assets, fontPath)
                binding.tvText.typeface = typeface
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (position == selectPosition) {
                binding.lnViewFont.background =
                    ContextCompat.getDrawable(context, R.drawable.bg_border_font)
                binding.ivChecked.visibility = View.VISIBLE
                binding.tvApply.visibility = View.GONE
            } else {
                binding.tvApply.visibility = View.VISIBLE
                binding.lnViewFont.background =
                    ContextCompat.getDrawable(context, R.drawable.bg_border_permission)
                binding.ivChecked.visibility = View.GONE
            }
            binding.tvApply.tapAndCheckInternet {
                fontClick.onCategoryClick(fontModel, position)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(fontModel: List<FontModel>) {
        if (fontModel.isEmpty()) {
            fontList = emptyList()
            notifyDataSetChanged()
            return
        }
        val updatedFontList = mutableListOf<FontModel>()
        fontModel.forEachIndexed { index, fontModel ->
            updatedFontList.add(fontModel)
            if (index == 1 || index == 7 || index == 13) {
                updatedFontList.add(FontModel(-1, "", "", true))
            }
        }
        fontList = updatedFontList
        notifyDataSetChanged()
    }

    inner class AdsViewHolder(val binding: ItemFontAdsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isLoadAds = false

        fun bind(fontModel: FontModel, position: Int) {
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

    interface FontClickListener {
        fun onCategoryClick(fontModel: FontModel, position: Int)
    }
}
