package com.sticker.nicekeyboard.ui.custom_background

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
import androidx.recyclerview.widget.RecyclerView
import com.sticker.nicekeyboard.MyApplication
import com.sticker.nicekeyboard.R
import com.sticker.nicekeyboard.base.BaseActivity
import com.sticker.nicekeyboard.call_api.ImageItem
import com.sticker.nicekeyboard.custom.ObjectTheme
import com.sticker.nicekeyboard.custom.ShareTheme
import com.sticker.nicekeyboard.database.MyBackgroundDAO
import com.sticker.nicekeyboard.database.MyBackgroundDataBase
import com.sticker.nicekeyboard.databinding.ActivityBackgroundBinding
import com.sticker.nicekeyboard.ui.custom_background.background_call_api.BackgroundDetailActivity
import com.sticker.nicekeyboard.ui.scale_image_background.CropActivity
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

class BackgroundActivity : BaseActivity<ActivityBackgroundBinding, BackgroundViewModel>() {
    private lateinit var myBackgroundDAO: MyBackgroundDAO
    private val REQUEST_CAMERA_PERMISSION = 103
    private lateinit var currentPhotoPath: String
    private var cameraPermissionDeniedCount = 0
    private var isClick = false

    override fun setBinding(layoutInflater: LayoutInflater): ActivityBackgroundBinding {
        return ActivityBackgroundBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): BackgroundViewModel {
        myBackgroundDAO = MyBackgroundDataBase.getInstance(application).myBackgroundDao()
        return BackgroundViewModel(myBackgroundDAO)
    }

    override fun initView() {
        getCategory()
        getMyBackground()
    }

    override fun bindView() {
        binding.toolbar.tvToolbar.text = getString(R.string.background)
        binding.toolbar.back.tapAndCheckInternet {
            onBackPressedDispatcher.onBackPressed()
            finish()
        }
        binding.tvSeeAllMyBackground.tapAndCheckInternet {
            val intent =
                Intent(this@BackgroundActivity, BackgroundDetailActivity::class.java)

            intent.putExtra(Constants.CATEGORY, Constants.CATEGORY_MY_BACKGROUND)
            startActivity(intent)
        }
    }

    private fun getCategory() {
        binding.rcyCategory.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val adapter = CategoryBackgroundAdapter(
            this,
            mutableListOf(),
            object : CategoryBackgroundAdapter.CategoryClickAdapter {
                override fun onCategoryClick(categoryWithImages: String) {
                    val intent =
                        Intent(this@BackgroundActivity, BackgroundDetailActivity::class.java)

                    intent.putExtra(Constants.CATEGORYNAME, categoryWithImages)
                    intent.putExtra(Constants.CATEGORY, Constants.CATEGORY_API)
                    startActivity(intent)
                }

            }, object : CategoryBackgroundAdapter.ImageClickAdapter {
                override fun onImageClick(
                    categoryWithImages: String,
                    imageItem: ImageItem,
                    position: Int
                ) {

                    if (!Constants.isMyKeyboardEnabled(this@BackgroundActivity) || !Constants.isMyKeyboardActive(
                            this@BackgroundActivity
                        ) || !checkStoragePermission()
                    ) {
                        dialogPermission()
                    } else {
                        if (!isClick) {
                            lifecycleScope.launch {
                                val path =
                                    downloadAndSaveImage(Constants.BASE_URL_IMAGE + imageItem.image)
                                if (path != null) {
                                    notifyMediaScanner(path)
                                    val objectTheme: ObjectTheme =
                                        ShareTheme.getmIntance(this@BackgroundActivity)
                                            .getObjectTheme()
                                    objectTheme.backgroundKeyboard.isBackground = true
                                    objectTheme.backgroundKeyboard.setIsAssets(false)
                                    objectTheme.backgroundKeyboard.setmPath_bg(path)
                                    ShareTheme.getmIntance(this@BackgroundActivity)
                                        .saveData(objectTheme)
                                    startActivity(
                                        Intent(
                                            this@BackgroundActivity,
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
        binding.rcyCategory.adapter = adapter
        viewModel.fetchImages()
        viewModel.imageCategories.observe(this, Observer { images ->
            adapter.updateData(images)
        })
    }

    private fun getMyBackground() {
        binding.rcyMyBackground.layoutManager =
            GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false)
        val adapter = MyBackgroundAdapter(
            this,
            mutableListOf(),
            object : MyBackgroundAdapter.MyBackgroundClickAdapter {
                override fun onMyBackgroundClick(imageItem: ImageItem, position: Int) {
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
                        if (!Constants.isMyKeyboardEnabled(this@BackgroundActivity) || !Constants.isMyKeyboardActive(
                                this@BackgroundActivity
                            ) || !checkStoragePermission()
                        ) {
                            dialogPermission()
                        } else {
                            val intent =
                                Intent(this@BackgroundActivity, CropActivity::class.java)
                            intent.putExtra(Constants.PATH_BACKGROUND_CROP,imageItem.image)
                            startActivity(intent)
                        }
                    }
                }

            })
        binding.rcyMyBackground.adapter = adapter
        viewModel.fetchMyBackgrounds()
        viewModel.myBackgrounds.observe(this) { backgrounds ->
            adapter.updateData(backgrounds)
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
                MyApplication.offAppOpen()
                val imageUri = Uri.fromFile(File(currentPhotoPath))
                // Lưu vào cơ sở dữ liệu hoặc xử lý ảnh ở đây
                saveImageToDatabase(imageUri.toString())
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
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
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