package com.sticker.nicekeyboard.ui.permission

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import com.sticker.nicekeyboard.MyApplication
import com.sticker.nicekeyboard.R
import com.sticker.nicekeyboard.base.BaseActivity
import com.sticker.nicekeyboard.databinding.ActivityPermissionBinding
import com.sticker.nicekeyboard.ui.main.MainActivity
import com.sticker.nicekeyboard.util.Constants
import com.sticker.nicekeyboard.util.tapAndCheckInternet

class PermissionActivity : BaseActivity<ActivityPermissionBinding, PermissionViewModel>() {
    private val READ_EXTERNAL_STORAGE = 100
    private var countStorage = 0
    private var isTurnOnKeyboard: Boolean = false
    private var isSelectKeyboard: Boolean = false
    private var isReadStorage: Boolean = false
    override fun setBinding(layoutInflater: LayoutInflater): ActivityPermissionBinding {
        return ActivityPermissionBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): PermissionViewModel {
        return PermissionViewModel()
    }

    override fun initView() {
        binding.ivTurnOnKeyboard.tapAndCheckInternet {
            MyApplication.offAppOpen()
            val intent = Intent("android.settings.INPUT_METHOD_SETTINGS")
            startActivity(intent)
        }
        binding.ivSelectKeyboard.tapAndCheckInternet {
            MyApplication.offAppOpen()
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showInputMethodPicker()
            val filter = IntentFilter(Intent.ACTION_INPUT_METHOD_CHANGED)
            registerReceiver(brDefaultKeyBoard, filter)
        }
        binding.ivReadStorage.tapAndCheckInternet {
            if (!checkStoragePermission()) {
                if (countStorage >= 2 && !shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES)) {
                    openAppSettings()
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                        countStorage++
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                            READ_EXTERNAL_STORAGE
                        )
                    } else {
                        countStorage++
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ),
                            READ_EXTERNAL_STORAGE
                        )
                    }
                }
            }
        }
        binding.tvContinue.tapAndCheckInternet {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }

    override fun bindView() {

    }

    private fun updateDialogUI() {
        if (Constants.isMyKeyboardEnabled(this)) {
            binding.ivTurnOnKeyboard.setImageResource(R.drawable.ic_check_done)
            binding.ivTurnOnKeyboard.isEnabled = false
            isTurnOnKeyboard = true
        }
        if (Constants.isMyKeyboardActive(this)) {
            binding.ivSelectKeyboard.setImageResource(R.drawable.ic_check_done)
            binding.ivSelectKeyboard.isEnabled = false
            isSelectKeyboard = true
        }
        if (checkStoragePermission()) {
            binding.ivReadStorage.setImageResource(R.drawable.ic_check_done)
            binding.ivReadStorage.isEnabled = false
            isReadStorage = true
        }
    }

    private var brDefaultKeyBoard: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == Intent.ACTION_INPUT_METHOD_CHANGED) {
                if (Constants.isMyKeyboardActive(context)) {
                    binding.ivSelectKeyboard.setImageResource(R.drawable.ic_check_done)
                    binding.ivSelectKeyboard.isEnabled = false
                    isSelectKeyboard = true
                    updateDialogUI()
                }
                context.unregisterReceiver(this)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                binding.ivReadStorage.setImageResource(R.drawable.ic_check_done)
                isReadStorage = true
                binding.ivReadStorage.isEnabled = false
                countStorage = 0
            } else {
                countStorage++
                binding.ivReadStorage.setImageResource(R.drawable.ic_turn_off_per)
            }
            updateDialogUI()
        }
    }

    override fun onResume() {
        super.onResume()
        updateDialogUI()
    }

}