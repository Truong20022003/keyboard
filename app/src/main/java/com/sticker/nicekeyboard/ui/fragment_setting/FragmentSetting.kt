package com.sticker.nicekeyboard.ui.fragment_setting

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.sticker.nicekeyboard.BuildConfig

import com.sticker.nicekeyboard.R
import com.sticker.nicekeyboard.base.BaseFragment
import com.sticker.nicekeyboard.databinding.FragmentSettingBinding
import com.sticker.nicekeyboard.rate.RatingDialog
import com.sticker.nicekeyboard.ui.language.LanguageSettingActivity
import com.sticker.nicekeyboard.ui.policy.PolicyActivity
import com.sticker.nicekeyboard.util.Data
import com.sticker.nicekeyboard.util.tapAndCheckInternet


class FragmentSetting : BaseFragment<FragmentSettingBinding, FragmentSettingViewModel>() {
    override fun setViewModel(): FragmentSettingViewModel {
        return FragmentSettingViewModel()
    }

    override fun getViewBinding(): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(layoutInflater)
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        binding.tvVersion.text = getString(R.string.version) + " " + BuildConfig.VERSION_NAME
        binding.cnLanguage.tapAndCheckInternet {
            startActivity(Intent(requireContext(), LanguageSettingActivity::class.java))
        }
        binding.cnRate.tapAndCheckInternet {
            showRatingDialog()
        }
        binding.cnShare.tapAndCheckInternet {
            viewModel.share(requireContext())
        }
        binding.cnPolicy.tapAndCheckInternet {
            startActivity(Intent(requireContext(), PolicyActivity::class.java))
        }
        if (Data.isRated()) {
            binding.cnRate.visibility = View.GONE
        }
    }

    override fun bindView() {

    }

    private fun showRatingDialog() {
        val ratingDialog =
            RatingDialog(requireContext())
        ratingDialog.setOnPress(object : RatingDialog.OnPress {

            override fun cancel() {

            }

            override fun later() {
                ratingDialog.dismiss()
            }

            override fun rating() {
                val manager: ReviewManager = ReviewManagerFactory.create(requireContext())
                val request: Task<ReviewInfo> =
                    manager.requestReviewFlow()
                request.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val reviewInfo: ReviewInfo = task.result
                        val flow: Task<Void> =
                            manager.launchReviewFlow(requireActivity(), reviewInfo)
                        flow.addOnSuccessListener {
                            Data.forceRated()
                            ratingDialog.dismiss()
                            dialogAfterRate()
                            binding.cnRate.visibility = View.GONE
                        }
                    } else {
                        ratingDialog.dismiss()
                    }
                }
            }

            override fun send(rate: Float) {
                Data.forceRated()
                Toast.makeText(
                    requireContext(),
                    requireContext().getString(R.string.thank_you_for_rating_us),
                    Toast.LENGTH_SHORT
                ).show()
                binding.cnRate.visibility = View.GONE
                ratingDialog.dismiss()
                dialogAfterRate()
            }
        })
        ratingDialog.show()
    }

    fun dialogAfterRate() {
        val builder = AlertDialog.Builder(requireContext(), R.style.full_screen_dialog)
        val inflater = LayoutInflater.from(requireContext())
        val dialogView = inflater.inflate(R.layout.dialog_thank_you, null)
        builder.setView(dialogView)
        val dialog = builder.create()
        val tvGotIt = dialogView.findViewById<TextView>(R.id.tvGotIt)
        val imgClose = dialogView.findViewById<ImageView>(R.id.imgClose)
        tvGotIt.setOnClickListener { dialog.dismiss() }
        imgClose.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }
}