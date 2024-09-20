package com.sticker.nicekeyboard.ui.fragment_theme

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.sticker.nicekeyboard.MyApplication
import com.sticker.nicekeyboard.R
import com.sticker.nicekeyboard.base.BaseFragment
import com.sticker.nicekeyboard.databinding.DialogStepPermissionBinding
import com.sticker.nicekeyboard.databinding.FragmentThemeBinding
import com.sticker.nicekeyboard.remote.RemoteConfig
import com.sticker.nicekeyboard.sdklisten.manager.AdsInterManager
import com.sticker.nicekeyboard.sdklisten.manager.AdsNativeManager
import com.sticker.nicekeyboard.ui.fragment_theme.category.CategoryAdapter
import com.sticker.nicekeyboard.ui.fragment_theme.category.CategoryModel
import com.sticker.nicekeyboard.ui.fragment_theme.theme.ThemeAdapter
import com.sticker.nicekeyboard.ui.fragment_theme.theme.ThemeModel
import com.sticker.nicekeyboard.ui.preview.PreviewActivity
import com.sticker.nicekeyboard.util.Constants
import com.sticker.nicekeyboard.util.tapAndCheckInternet

import java.util.Objects

class FragmentTheme : BaseFragment<FragmentThemeBinding, FragmentThemeViewModel>() {
    private lateinit var adapter: CategoryAdapter
    private lateinit var themeAdapter: ThemeAdapter
    private var listCategory: List<CategoryModel> = mutableListOf()

    private lateinit var dialogBinding: DialogStepPermissionBinding
    private lateinit var dialog: Dialog

    private var mCount: Int = 0
    override fun setViewModel(): FragmentThemeViewModel {
        return FragmentThemeViewModel()
    }

    override fun getViewBinding(): FragmentThemeBinding {
        return FragmentThemeBinding.inflate(layoutInflater)
    }

    override fun initView() {
        getCategory()
    }

    override fun bindView() {

        binding.tvApplyKeyboard.tapAndCheckInternet {
            if (!Constants.isMyKeyboardEnabled(requireContext()) || !Constants.isMyKeyboardActive(
                    requireContext()
                )
            ) {
                dialogPermissionStep()
            }
        }
    }

    private fun getCategory() {
        adapter = CategoryAdapter(
            requireContext(),
            emptyList(),
            object : CategoryAdapter.CategoryClickListener {
                override fun onCategoryClick(category: CategoryModel) {
                }

            })
        binding.rcyCategory.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = this@FragmentTheme.adapter
        }

        val categories = listOf(
            CategoryModel(
                getString(R.string.Neon),
                false,
                Constants.NEON_THEME,
                Constants.NEON_THEME_BG,
                emptyList()
            ),
            CategoryModel(
                getString(R.string.Basic),
                false,
                Constants.CHRISTMAS_THEME,
                Constants.CHRISTMAS_THEME_BG,
                emptyList()
            ),
            CategoryModel(
                getString(R.string.Cute),
                false,
                Constants.CUTE_THEME,
                Constants.CUTE_THEME_BG,
                emptyList()
            ),
            CategoryModel(
                getString(R.string.Halloween),
                false,
                Constants.HALLOWEEN_THEME,
                Constants.HALLOWEEN_THEME_BG,
                emptyList()
            ),
            CategoryModel(
                getString(R.string.Colors),
                false,
                Constants.NEW_YEAR_THEME,
                Constants.NEW_YEAR_THEME_BG,
                emptyList()
            )
        )
        viewModel.categories.observe(viewLifecycleOwner) { updatedCategories ->
            adapter = CategoryAdapter(
                requireContext(),
                updatedCategories,
                object : CategoryAdapter.CategoryClickListener {
                    override fun onCategoryClick(category: CategoryModel) {
                        updateThemesForCategory(category)
                    }
                })
            binding.rcyCategory.adapter = adapter

            listCategory = updatedCategories


            if (listCategory.isNotEmpty()) {
                updateThemesForCategory(listCategory[0])
            }
            Log.d("listCategory", listCategory.toString())
        }
        viewModel.loadThemesForCategories(requireContext(), categories)


    }

    private fun updateThemesForCategory(category: CategoryModel) {
        themeAdapter = ThemeAdapter(
            requireContext(),
            category.listThemeModel,
            object : ThemeAdapter.ThemeClick {
                override fun onClickItem(themeModel: ThemeModel, position: Int) {
                    mCount++
                    if (mCount == 1) {
                        val intent = Intent(context, PreviewActivity::class.java)
                        val bundle = Bundle().apply {
                            putParcelableArrayList(
                                Constants.BUNDLE_THEME_LIST,
                                ArrayList(category.listThemeModel)
                            )
                        }
                        intent.putExtras(bundle)
                        intent.putExtra(Constants.BUNDLE_POSITION, position)
                        intent.putExtra(Constants.BUNDLE_FOLDER_NAME, category.folderName)
                        intent.putExtra(
                            Constants.BUNDLE_FOLDER_BACKGROUND,
                            category.folderBackground
                        )
                        startActivity(intent)
                    } else {
                        AdsInterManager.showInter(requireActivity(), RemoteConfig.inter_theme) {
                            val intent = Intent(context, PreviewActivity::class.java)
                            val bundle = Bundle().apply {
                                putParcelableArrayList(
                                    Constants.BUNDLE_THEME_LIST,
                                    ArrayList(category.listThemeModel)
                                )
                            }
                            intent.putExtras(bundle)
                            intent.putExtra(Constants.BUNDLE_POSITION, position)
                            intent.putExtra(Constants.BUNDLE_FOLDER_NAME, category.folderName)
                            intent.putExtra(
                                Constants.BUNDLE_FOLDER_BACKGROUND,
                                category.folderBackground
                            )
                            startActivity(intent)
                        }
                    }
                }
            })

        binding.rcyTheme.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = themeAdapter
        }
    }

    private fun dialogPermissionStep() {
        dialogBinding = DialogStepPermissionBinding.inflate(layoutInflater)
        dialog = Dialog(requireContext())
        dialogBinding.root.let { dialog.setContentView(it) }
        Objects.requireNonNull(dialog.window)?.setGravity(Gravity.CENTER)
        dialog.window?.setLayout(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        updateDialogUI()
        AdsNativeManager.loadAdsNative1(
            dialogBinding.root,
            R.layout.ads_native_small,
            RemoteConfig.native_apply,
            R.layout.ads_shimmer_native_small
        )
        dialogBinding.tvStep1.setOnClickListener {
            if (!Constants.isMyKeyboardEnabled(requireContext())) {
                MyApplication.offAppOpen()
                startActivity(Intent("android.settings.INPUT_METHOD_SETTINGS"))
            }
        }

        dialogBinding.tvStep2.setOnClickListener {
            if (!Constants.isMyKeyboardActive(requireContext())) {
                MyApplication.offAppOpen()
                val inputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showInputMethodPicker()
                val filter = IntentFilter(Intent.ACTION_INPUT_METHOD_CHANGED)
                requireContext().registerReceiver(brDefaultKeyBoard, filter)
            }
        }

        dialogBinding.tvApply.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        if (!Constants.isMyKeyboardEnabled(requireContext()) || !Constants.isMyKeyboardActive(
                requireContext()
            )
        ) {
            binding.tvApplyKeyboard.visibility = View.VISIBLE
        }
        if (::dialog.isInitialized && dialog.isShowing) {
            updateDialogUI()

        }
    }

    private fun updateDialogUI() {
        if (Constants.isMyKeyboardEnabled(requireContext())) {
            dialogBinding.tvStep1.setBackgroundResource(R.drawable.bg_step_per)
            dialogBinding.tvStep1.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_039732
                )
            )
            dialogBinding.ivStep2.setImageResource(R.drawable.ic_step_2_select)
            dialogBinding.tvStep1.text = getString(R.string.step_1_done)
            dialogBinding.tvStep2.alpha = 1f
            dialogBinding.tvStep2.isEnabled = true
            dialogBinding.pb.progress = 50
        } else {
            dialogBinding.tvStep1.setBackgroundResource(R.drawable.bg_button_step_per_select)
            dialogBinding.tvStep1.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            dialogBinding.tvStep1.text = getString(R.string.step_1_select)
            dialogBinding.tvStep2.alpha = 0.5f
            dialogBinding.tvStep2.isEnabled = false
            dialogBinding.pb.progress = 8
        }
    }

    private var brDefaultKeyBoard: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == Intent.ACTION_INPUT_METHOD_CHANGED) {
                if (Constants.isMyKeyboardActive(context)) {
                    dialogBinding.ivStep3.setImageResource(R.drawable.ic_step3_select)
                    dialogBinding.pb.progress = 100
                    dialogBinding.lnViewClick.visibility = View.GONE
                    dialogBinding.tvFind.visibility = View.VISIBLE
                    dialogBinding.tvTop.text = getString(R.string.congratulation)
                    binding.tvApplyKeyboard.visibility = View.GONE
                }
                context.unregisterReceiver(this)
            }
        }
    }


}