package com.sticker.nicekeyboard.base

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.sticker.nicekeyboard.MyApplication
import com.sticker.nicekeyboard.R
import com.sticker.nicekeyboard.databinding.DialogPermissionBinding
import com.sticker.nicekeyboard.util.Constants
import com.sticker.nicekeyboard.util.SystemUtil
import java.util.Objects


abstract class BaseActivity<T : ViewBinding, V : ViewModel> : AppCompatActivity() {

    lateinit var binding: T
    lateinit var viewModel: V

    abstract fun setBinding(layoutInflater: LayoutInflater): T
    abstract fun setViewModel(): V
    abstract fun initView()
    abstract fun bindView()
    private lateinit var bindingDialog: DialogPermissionBinding
    private lateinit var dialog: Dialog
    private var isTurnOnKeyboard: Boolean = false
    private var isSelectKeyboard: Boolean = false
    private var isReadStorage: Boolean = false
    private val REQUEST_PERMISSIONS = 101
    private var countStorage = 0
    private lateinit var dialogLoading: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemUtil.setLocale(this)
        setContentView(getInflatedLayoutNew(layoutInflater))
        viewModel = setViewModel()
        setupWindow()
        hideFullNavigation()
        initView()
        bindView()
    }

    private fun getInflatedLayoutNew(inflater: LayoutInflater): View {
        binding = setBinding(inflater)
        return binding.root
    }

    private fun setupWindow() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = Color.WHITE
        window.navigationBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility =
            if (VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            } else {
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        binding.root.setPadding(
            binding.root.paddingLeft,
            binding.root.paddingTop + getStatusBarHeight(),
            binding.root.paddingRight,
            binding.root.paddingBottom
        )
    }

    private fun getStatusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    fun startNextActivity(activity: Class<*>, bundle: Bundle? = null) {
        val intent = Intent(this, activity).apply {
            putExtras(bundle ?: Bundle())
        }
        startActivity(intent)
    }

    private fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        winParams.flags = if (on) {
            winParams.flags or bits
        } else {
            winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    private fun hideFullNavigation() {
        val windowInsetsController = if (VERSION.SDK_INT >= 30) {
            ViewCompat.getWindowInsetsController(window.decorView)
        } else {
            WindowInsetsControllerCompat(window, binding.root)
        } ?: return

        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())

        window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if (visibility == 0) {
                Handler().postDelayed({
                    val controller = if (VERSION.SDK_INT >= 30) {
                        ViewCompat.getWindowInsetsController(window.decorView)
                    } else {
                        WindowInsetsControllerCompat(window, binding.root)
                    }
                    controller?.hide(WindowInsetsCompat.Type.navigationBars())
                }, 3000)
            }
        }
    }


    fun hideView(view: View) {
        view.visibility = View.GONE
    }

    fun showView(view: View) {
        view.visibility = View.VISIBLE
    }

    fun invisibleView(view: View) {
        view.visibility = View.INVISIBLE
    }

    fun checkStoragePermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        } else {
            if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                return ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
            }
            return false
        }
    }

    protected fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun dialogPermission() {
        bindingDialog = DialogPermissionBinding.inflate(layoutInflater)
        dialog = Dialog(this)
        dialog.setContentView(bindingDialog.root)
        Objects.requireNonNull(dialog.window)?.setGravity(Gravity.CENTER)
        dialog.window!!.setLayout(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        updateDialogUI()

        bindingDialog.ivTurnOnKeyboard.setOnClickListener {
            MyApplication.offAppOpen()
            val intent = Intent("android.settings.INPUT_METHOD_SETTINGS")
            startActivity(intent)
        }
        bindingDialog.ivSelectKeyboard.setOnClickListener {
            MyApplication.offAppOpen()
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showInputMethodPicker()
            val filter = IntentFilter(Intent.ACTION_INPUT_METHOD_CHANGED)
            registerReceiver(brDefaultKeyBoard, filter)
        }
        bindingDialog.ivReadStorage.setOnClickListener {
            if (!checkStoragePermission()) {
                if (countStorage >= 2 && !shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES)) {
                    openAppSettings()
                } else {
                    if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                        countStorage++
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                            REQUEST_PERMISSIONS
                        )
                    } else {
                        countStorage++
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ),
                            REQUEST_PERMISSIONS
                        )
                    }
                }
            }
        }
        bindingDialog.tvCancel.setOnClickListener {
            dialog.dismiss()
        }
        bindingDialog.tvContinue.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        if (::dialog.isInitialized && dialog.isShowing) {
            updateDialogUI()
        }

    }

    fun showLoading() {
        dialogLoading = Dialog(this)
        dialogLoading.setContentView(R.layout.dialog_loading)
        Objects.requireNonNull(dialogLoading.window)?.setGravity(Gravity.CENTER)
        dialogLoading.window?.setLayout(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        dialogLoading.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLoading.setCancelable(false)
        dialogLoading.show()
    }

    fun hideLoading() {
        if (dialogLoading.isShowing) {
            dialogLoading.dismiss()
        }
    }

    private fun updateDialogUI() {
        if (Constants.isMyKeyboardEnabled(this)) {
            bindingDialog.ivTurnOnKeyboard.setBackgroundResource(R.drawable.ic_check_done)
            bindingDialog.ivTurnOnKeyboard.isEnabled = false
            isTurnOnKeyboard = true
        }
        if (Constants.isMyKeyboardActive(this)) {
            bindingDialog.ivSelectKeyboard.setBackgroundResource(R.drawable.ic_check_done)
            bindingDialog.ivSelectKeyboard.isEnabled = false
            isSelectKeyboard = true
        }
        if (checkStoragePermission()) {
            bindingDialog.ivReadStorage.setBackgroundResource(R.drawable.ic_check_done)
            bindingDialog.ivReadStorage.isEnabled = false
            isReadStorage = true
        }

        bindingDialog.tvContinue.isEnabled = isTurnOnKeyboard && isSelectKeyboard && isReadStorage
        bindingDialog.tvContinue.alpha =
            if (isTurnOnKeyboard && isSelectKeyboard && isReadStorage) 1f else 0.5f
    }

    private var brDefaultKeyBoard: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == Intent.ACTION_INPUT_METHOD_CHANGED) {
                if (Constants.isMyKeyboardActive(context)) {
                    bindingDialog.ivSelectKeyboard.setBackgroundResource(R.drawable.ic_check_done)
                    bindingDialog.ivSelectKeyboard.isEnabled = false
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
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                bindingDialog.ivReadStorage.setBackgroundResource(R.drawable.ic_check_done)
                isReadStorage = true
                bindingDialog.ivReadStorage.isEnabled = false
                countStorage = 0
            } else {
                countStorage++
                bindingDialog.ivReadStorage.setBackgroundResource(R.drawable.ic_turn_off_per)
            }
            updateDialogUI()
        }
    }

    fun openAppSettings() {
        MyApplication.offAppOpen()
        val intent = Intent()
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.setData(uri)
        startActivity(intent)
    }

    override fun attachBaseContext(newBase: Context?) {
        if (newBase == null) {
            super.attachBaseContext(null)
        }
        newBase?.let {
            val newContext = SystemUtil.setLocale(it)
            super.attachBaseContext(newContext)
        }
    }
}