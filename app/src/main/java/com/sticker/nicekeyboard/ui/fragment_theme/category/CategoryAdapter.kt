package com.sticker.nicekeyboard.ui.fragment_theme.category

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sticker.nicekeyboard.R
import com.sticker.nicekeyboard.databinding.ItemCategoryBinding



class CategoryAdapter(
    private val context: Context,
    private val categoryList: List<CategoryModel>,
    private val categoryClickListener: CategoryClickListener
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.bind(category)
    }

    override fun getItemCount(): Int = categoryList.size

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("NotifyDataSetChanged")
        fun bind(category: CategoryModel) {
            binding.tvCategoryName.text = category.categoryName
            val backgroundResource = if (category.isSelected) {
                R.drawable.bg_category_select
            } else {
                R.drawable.bg_category_unselect
            }
            binding.root.setBackgroundResource(backgroundResource)

            binding.root.setOnClickListener {
                categoryList.forEach { it.isSelected = false }
                category.isSelected = true
                notifyDataSetChanged()
                categoryClickListener.onCategoryClick(category)
            }
        }
    }

    interface CategoryClickListener {
        fun onCategoryClick(category: CategoryModel)
    }
}
