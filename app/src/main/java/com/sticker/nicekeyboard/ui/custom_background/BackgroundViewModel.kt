package com.sticker.nicekeyboard.ui.custom_background

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.sticker.nicekeyboard.call_api.ApiService
import com.sticker.nicekeyboard.call_api.CategoryWithImages
import com.sticker.nicekeyboard.call_api.ImageItem
import com.sticker.nicekeyboard.call_api.RetrofitClient
import com.sticker.nicekeyboard.database.MyBackgroundDAO
import com.sticker.nicekeyboard.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BackgroundViewModel(private val myBackgroundDAO: MyBackgroundDAO) : ViewModel() {
    private val _imageCategories = MutableLiveData<List<CategoryWithImages>>()
    val imageCategories: LiveData<List<CategoryWithImages>> get() = _imageCategories
    private val apiService = RetrofitClient.instance.create(ApiService::class.java)

    private val _myBackgrounds = MutableLiveData<List<ImageItem>>()
    val myBackgrounds: LiveData<List<ImageItem>> get() = _myBackgrounds

    fun fetchImages() {
        viewModelScope.launch {
            try {
                val imageItems = withContext(Dispatchers.IO) {
                    apiService.getImageItems()
                }

                // Group the ImageItem by category
                val groupedByCategory = imageItems.groupBy { it.category }

                // Convert to a list of CategoryWithImages
                val categoriesWithImages = groupedByCategory.map { (category, items) ->
                    CategoryWithImages(category, items, items.size > 6)
                }

                _imageCategories.postValue(categoriesWithImages)
            } catch (e: Exception) {
                Log.e("BackgroundViewModel", "Error fetching images: ${e.message}")
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
