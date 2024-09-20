package com.sticker.nicekeyboard.ui.scale_image_background

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.isseiaoki.simplecropview.CropImageView
import com.sticker.nicekeyboard.base.BaseActivity
import com.sticker.nicekeyboard.custom.ObjectTheme
import com.sticker.nicekeyboard.custom.ShareTheme
import com.sticker.nicekeyboard.databinding.ActivityCropBinding
import com.sticker.nicekeyboard.remote.RemoteConfig
import com.sticker.nicekeyboard.sdklisten.manager.AdsInterManager
import com.sticker.nicekeyboard.ui.success.SuccessActivity
import com.sticker.nicekeyboard.util.Constants
import com.sticker.nicekeyboard.util.tapAndCheckInternet
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CropActivity : BaseActivity<ActivityCropBinding, CropViewModel>() {

    private var mCropView: CropImageView? = null

    private var cropedbitmap: Bitmap? = null
    override fun setBinding(layoutInflater: LayoutInflater): ActivityCropBinding {
        return ActivityCropBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): CropViewModel {
        return CropViewModel()
    }

    override fun initView() {
        mCropView = binding.cropImageView
        mCropView?.setInitialFrameScale(1.0f)
        mCropView?.setHandleSizeInDp(8)
        mCropView?.setCropMode(CropImageView.CropMode.RATIO_16_9)
        mCropView?.setAnimationEnabled(false)
        mCropView?.setGuideShowMode(CropImageView.ShowMode.SHOW_ALWAYS)
        val intent = intent
        if (intent != null) {
            val pictureUriString = intent.getStringExtra(Constants.PATH_BACKGROUND_CROP)
            if (pictureUriString != null) {
                val pictureUri = Uri.parse(pictureUriString)
                mCropView?.setImageURI(pictureUri)
            }
        }

    }

    @SuppressLint("CheckResult")
    override fun bindView() {
        binding.back.tapAndCheckInternet {
            onBackPressedDispatcher.onBackPressed()
            finish()
        }
        binding.tvSave.tapAndCheckInternet {
            showLoading()
            cropedbitmap = mCropView?.croppedBitmap
            saveBitmapToFile(cropedbitmap!!)
                .subscribe({
                    Log.d("pathDownload", it.replace("file:///", ""))
                    hideLoading()
                    val objectTheme: ObjectTheme = ShareTheme.getmIntance(this@CropActivity).getObjectTheme()
                    objectTheme.backgroundKeyboard.isBackground = true
                    objectTheme.backgroundKeyboard.setIsAssets(false)
                    objectTheme.backgroundKeyboard.setmPath_bg(
                        it.replace(
                            "file:///",
                            ""
                        )
                    )
                    ShareTheme.getmIntance(this@CropActivity)
                        .saveData(objectTheme)
                    AdsInterManager.showInter(this@CropActivity, RemoteConfig.inter_save) {
                        startActivity(Intent(this@CropActivity, SuccessActivity::class.java))
                        finish()
                    }
                }, { throwable: Throwable ->
                    hideLoading()
                    Log.e("SaveBitmapToFile", "Error: " + throwable.message)
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                })
        }
    }

    private fun saveBitmapToFile(bitmap: Bitmap): Single<String> {
        return Single.fromCallable {
            val tempFile =
                File.createTempFile("temp_image", ".png", cacheDir)
            try {
                FileOutputStream(tempFile).use { fos ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                    fos.flush()
                }
            } catch (e: IOException) {
                throw RuntimeException("Error saving bitmap to file", e)
            }
            tempFile.absolutePath
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}