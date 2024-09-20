package com.sticker.nicekeyboard.ui.custom_background.background_call_api

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sticker.nicekeyboard.MyApplication
import com.sticker.nicekeyboard.R
import com.sticker.nicekeyboard.base.BaseActivity
import com.sticker.nicekeyboard.call_api.ImageItem
import com.sticker.nicekeyboard.custom.ObjectTheme
import com.sticker.nicekeyboard.custom.ShareTheme
import com.sticker.nicekeyboard.database.MyBackgroundDAO
import com.sticker.nicekeyboard.database.MyBackgroundDataBase
import com.sticker.nicekeyboard.databinding.ActivityBackgroundDetailBinding
import com.sticker.nicekeyboard.ui.custom_background.MyBackgroundAdapter
import com.sticker.nicekeyboard.ui.success.SuccessActivity
import com.sticker.nicekeyboard.util.Constants
import com.sticker.nicekeyboard.util.tapAndCheckInternet

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BackgroundDetailActivity :
    BaseActivity<ActivityBackgroundDetailBinding, BackgroundDetailViewModel>() {
    private lateinit var backgroundDetailAdapter: BackgroundDetailAdapter
    private var categoryName: String? = null
    private var categoryIntent: String? = null
    private lateinit var myBackgroundDAO: MyBackgroundDAO
    private val REQUEST_CAMERA_PERMISSION = 105
    private lateinit var currentPhotoPath: String
    private var cameraPermissionDeniedCount = 0
    private var isClick = false

    override fun setBinding(layoutInflater: LayoutInflater): ActivityBackgroundDetailBinding {
        return ActivityBackgroundDetailBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): BackgroundDetailViewModel {
        myBackgroundDAO = MyBackgroundDataBase.getInstance(application).myBackgroundDao()
        return BackgroundDetailViewModel(myBackgroundDAO)
    }

    override fun initView() {
        binding.toolbar.back.tapAndCheckInternet {
            onBackPressedDispatcher.onBackPressed()
            finish()
        }
    }

    override fun bindView() {
        categoryIntent = intent.getStringExtra(Constants.CATEGORY)
        if (categoryIntent.equals(Constants.CATEGORY_API)) {
            categoryName = intent.getStringExtra(Constants.CATEGORYNAME)
            Log.d("aaa", categoryName.toString())
            categoryName?.let {
                getListBackground(it)
                binding.toolbar.tvToolbar.text = it
            }
        } else if (categoryIntent.equals(Constants.CATEGORY_MY_BACKGROUND)) {
            getListMyBackground()
            binding.toolbar.tvToolbar.text = getString(R.string.my_background)
        }


    }


    private fun getListBackground(category: String) {
        backgroundDetailAdapter = BackgroundDetailAdapter(
            this,
            mutableListOf(),
            object : BackgroundDetailAdapter.BackgroundDetailClick {
                override fun onBackgroundDetailClick(imageItem: ImageItem, position: Int) {
                    Log.d("aaa", "onBackgroundDetailClick:   " + imageItem.image)
                    if (!Constants.isMyKeyboardEnabled(this@BackgroundDetailActivity) || !Constants.isMyKeyboardActive(
                            this@BackgroundDetailActivity
                        ) || !checkStoragePermission()
                    ) {
                        dialogPermission()
                    } else {
                        if (!isClick) {
                            lifecycleScope.launch {
                                val path =
                                    downloadAndSaveImage(Constants.BASE_URL_IMAGE + imageItem.image)
                                if (path != null) {
                                    Log.d("pathDownload", path)
                                    notifyMediaScanner(path)
                                    val objectTheme: ObjectTheme =
                                        ShareTheme.getmIntance(this@BackgroundDetailActivity)
                                            .getObjectTheme()
                                    objectTheme.backgroundKeyboard.isBackground = true
                                    objectTheme.backgroundKeyboard.setIsAssets(false)
                                    objectTheme.backgroundKeyboard.setmPath_bg(path)
                                    ShareTheme.getmIntance(this@BackgroundDetailActivity)
                                        .saveData(objectTheme)
                                    startActivity(
                                        Intent(
                                            this@BackgroundDetailActivity,
                                            SuccessActivity::class.java
                                        )
                                    )
                                } else {
                                    toast("Failed to download image")
                                }
                            }
                            isClick = true
                        }

                    }
                }

            })
        viewModel.fetchImages(category)
        viewModel.imageBackground.observe(this, Observer { images ->
            backgroundDetailAdapter.updateData(images)
        })

        binding.rcyBackground.apply {
            layoutManager = GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
            adapter = backgroundDetailAdapter
        }
    }

    private fun getListMyBackground() {
        binding.rcyBackground.layoutManager =
            GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false)
        val adapter = MyBackgroundAdapter(
            this,
            mutableListOf(),
            object : MyBackgroundAdapter.MyBackgroundClickAdapter {
                override fun onMyBackgroundClick(imageItem: ImageItem, position: Int) {
                    Log.d("aaa", "getListMyBackground:    " + imageItem.image)
                    if (position == 0) {
                        if (!checkCameraPermission()) {
                            if (cameraPermissionDeniedCount >= 2 && !shouldShowRequestPermissionRationale(
                                    Manifest.permission.CAMERA
                                )
                            ) {
                                openAppSettings()
                            } else {
                                cameraPermissionDeniedCount++
                                requestCameraPermission()
                            }
                        } else {
                            dispatchTakePictureIntent()
                        }
                    } else {
                        Log.d("pathDownload", imageItem.image.replace("file:///", ""))
                        if (!Constants.isMyKeyboardEnabled(this@BackgroundDetailActivity) || !Constants.isMyKeyboardActive(
                                this@BackgroundDetailActivity
                            ) || !checkStoragePermission()
                        ) {
                            dialogPermission()
                        } else {
                            val objectTheme: ObjectTheme =
                                ShareTheme.getmIntance(this@BackgroundDetailActivity)
                                    .getObjectTheme()
                            objectTheme.backgroundKeyboard.isBackground = true
                            objectTheme.backgroundKeyboard.setIsAssets(false)
                            objectTheme.backgroundKeyboard.setmPath_bg(
                                imageItem.image.replace(
                                    "file:///",
                                    ""
                                )
                            )
                            ShareTheme.getmIntance(this@BackgroundDetailActivity)
                                .saveData(objectTheme)
                            startActivity(
                                Intent(
                                    this@BackgroundDetailActivity,
                                    SuccessActivity::class.java
                                )
                            )
                        }
                    }

                }

            })
        binding.rcyBackground.adapter = adapter
        viewModel.fetchMyBackgrounds()
        viewModel.myBackgrounds.observe(this) { backgrounds ->
            adapter.updateData(backgrounds)
        }
    }

    private suspend fun downloadAndSaveImage(imageUrl: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val request = Request.Builder().url(imageUrl).build()
                val response = client.newCall(request).execute()
                val inputStream: InputStream? = response.body?.byteStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)

                val filename = "downloaded_image_${System.currentTimeMillis()}.png"
                val directory = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        .toString() + "/DownloadedImages"
                )

                if (!directory.exists()) {
                    directory.mkdirs()
                }

                val file = File(directory, filename)
                val outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()
                outputStream.close()

                file.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun notifyMediaScanner(path: String) {
        MediaScannerConnection.scanFile(this, arrayOf(path), null) { _, uri ->
            Log.d("BackgroundDetailActivity", "Scanned $path:")
            Log.d("BackgroundDetailActivity", "-> uri=$uri")
        }
    }

    private fun checkCameraPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            REQUEST_CAMERA_PERMISSION
        )
    }

    private var resultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = Uri.fromFile(File(currentPhotoPath))
                val imagePath = imageUri.path
                if (imagePath != null) {
                    saveImageToDatabase(imagePath)
                }
            }else if (result.resultCode == Activity.RESULT_CANCELED){
                MyApplication.offAppOpen()
            }else{
                MyApplication.offAppOpen()
            }
        }

    @SuppressLint("QueryPermissionsNeeded")
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                MyApplication.offAppOpen()
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "$packageName.provider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    resultLauncher.launch(takePictureIntent)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val filename = "downloaded_image_${timeStamp}.png"
        val directory = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/DownloadedImages"
        )

        if (!directory.exists()) {
            directory.mkdirs()
        }

        return File(directory, filename).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun saveImageToDatabase(imagePath: String) {
        val imageItem = ImageItem(
            id = 0,
            thumbnail = imagePath,
            image = imagePath,
            category = "my_background"
        )
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                myBackgroundDAO.insert(imageItem)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    cameraPermissionDeniedCount = 0
                    dispatchTakePictureIntent()
                } else {
                    cameraPermissionDeniedCount++
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        isClick = false
    }
}