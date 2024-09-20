package com.sticker.nicekeyboard.ui.language

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sticker.nicekeyboard.R


class LanguageStartAdapter(
    private val context: Context,
    private var list: List<LanguageModel>,
    private val listener: Listener
) : RecyclerView.Adapter<LanguageStartAdapter.CountryHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<LanguageModel>) {
        list = newList
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSelectedLanguage(selectedLanguage: LanguageModel) {
        list.forEach { it.active = it == selectedLanguage }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_language, parent, false)
        return CountryHolder(view)
    }

    override fun onBindViewHolder(holder: CountryHolder, position: Int) {
        val data = list[position]
        holder.bind(data, context)
        Glide.with(context).load(data.image).into(holder.ivAvatar)
        holder.itemView.setOnClickListener { listener.onClickLanguage(data) }
    }

    override fun getItemCount() = list.size

    class CountryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivAvatar: ImageView = itemView.findViewById(R.id.ivLanguage)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvNameLanguage)
        private val cnViewLanguage: ConstraintLayout = itemView.findViewById(R.id.cnViewLanguage)

        fun bind(languageModel: LanguageModel, context: Context) {
            tvTitle.text = languageModel.languageName
            cnViewLanguage.setBackgroundResource(
                if (languageModel.active) R.drawable.bg_border_language else R.drawable.bg_border_language_unselect
            )
            val typefaceRes = if (languageModel.active) {
                R.font.configrounded_600
            } else {
                R.font.configrounded_400
            }
            tvTitle.typeface = ResourcesCompat.getFont(context, typefaceRes)
            val colorText = if (languageModel.active) {
                ContextCompat.getColor(context, R.color.white)
            } else {
                ContextCompat.getColor(context, R.color.color_333841)
            }
            tvTitle.setTextColor(colorText)
        }
    }

    interface Listener {
        fun onClickLanguage(languageModel: LanguageModel)
    }
}
