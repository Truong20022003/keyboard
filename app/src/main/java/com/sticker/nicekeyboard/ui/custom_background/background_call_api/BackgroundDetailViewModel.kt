package com.sticker.nicekeyboard.ui.custom_background.background_call_api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sticker.nicekeyboard.call_api.ApiService
import com.sticker.nicekeyboard.call_api.ImageItem
import com.sticker.nicekeyboard.call_api.RetrofitClient
import com.sticker.nicekeyboard.database.MyBackgroundDAO
import com.sticker.nicekeyboard.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BackgroundDetailViewModel(private val myBackgroundDAO: MyBackgroundDAO) : ViewModel() {
    private val _imageBackground = MutableLiveData<List<ImageItem>>()
    val imageBackground: LiveData<List<ImageItem>> get() = _imageBackground

    private val apiService = RetrofitClient.instance.create(ApiService::class.java)

    private val _myBackgrounds = MutableLiveData<List<ImageItem>>()
    val myBackgrounds: LiveData<List<ImageItem>> get() = _myBackgrounds

    fun fetchImages(category: String) {
        viewModelScope.launch {
            try {
                val imageItems = withContext(Dispatchers.IO) {
                    apiService.getImageItemsForCategory(category)
                }

                _imageBackground.postValue(imageItems)
                Log.e("aaa", "${imageItems}")
            } catch (e: Exception) {
                Log.e("aaa", "Error fetching images: ${e.message}")
            }
        }
    }

    fun fetchMyBackgrounds() {
        viewModelScope.launch {
            try {
                // Sử dụng LiveData để theo dõi thay đổi và cập nhật dữ liệu
                val backgroundsLiveData = myBackgroundDAO.getAllMyBackground()

                // Sử dụng MediatorLiveData để nhận dữ liệu từ LiveData
                val mediatorLiveData = MediatorLiveData<List<ImageItem>>()

                mediatorLiveData.addSource(backgroundsLiveData) { backgrounds ->
                    val newList = mutableListOf<ImageItem>().apply {
                        add(
                            ImageItem(
                                0,
                                Constants.MY_BACKGROUND,
                                Constants.MY_BACKGROUND,
                                "my_background"
                            )
                        )
                        addAll(backgrounds)
                    }

                    mediatorLiveData.postValue(newList)
                }

                mediatorLiveData.observeForever { updatedList ->
                    _myBackgrounds.postValue(updatedList)
                }
            } catch (e: Exception) {
                Log.e("BackgroundViewModel", "Error fetching backgrounds: ${e.message}")
            }
        }
    }


}