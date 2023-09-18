package com.mihailtarasev.flickrgallery.scene.main.usecase

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mihailtarasev.flickrgallery.extension.trigger
import com.mihailtarasev.flickrgallery.scene.main.model.MainNetworkModel
import com.mihailtarasev.flickrgallery.scene.main.model.MainModel
import com.mihailtarasev.flickrgallery.scene.main.model.MainPhotoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel: ViewModel() {
    private val networkGateway = MainNetworkGatewayImp()
    private var model = MainModel(
        defaultText,
        defaultCurrentPage,
        defaultPhotoPages,
        defaultPhotoList,
        defaultIsRefreshing
    )
    var pageClearedLiveData = MutableLiveData<Void>()
    var photoListUpdatedLiveData = MutableLiveData<ArrayList<MainPhotoModel>>()
    var isRefreshingLiveData = MutableLiveData<Boolean>()
    var isAlertFiredLiveData = MutableLiveData<String>()

    fun uploadFirstPage() {
        if(isNextPageWillFirst()) {
            uploadNextAvailablePage()
        }
    }

    fun updateNotFilteredClearedPage() {
        if(isNotFiltered()) {
            updateClearedPage()
        }
    }

    fun updateClearedPage() {
        clearPage()
        uploadNextAvailablePage()
    }

    fun uploadNextAvailablePage() {
        if (model.isAvailableNextPage()) {
            emitIsRefreshing(true)
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val networkGatewayModel = getNextPhotoPage()
                    model.updateBy(networkGatewayModel)
                    withContext(Dispatchers.Main) {
                        emitPhotoListUpdatedToObserver()
                    }
                } catch (exception: Exception) {
                    withContext(Dispatchers.Main) {
                        emitIsAlertFired(exception.toString())
                    }
                } finally {
                    withContext(Dispatchers.Main) {
                        emitIsRefreshing(false)
                    }
                }
            }
        }
    }

    fun setFilterWithDefault(text: String?, default: String = defaultText) { model.text = text ?: default }

    private suspend fun getNextPhotoPage(): MainNetworkModel {
        val nextPageNumber = model.currentPageNumber + 1
        val text = model.text.ifEmpty { defaultText }
        return networkGateway.getPhotoPageRequest(text, defaultPerPage, nextPageNumber)
    }

    private fun clearPage() {
        model.clearPage()
        emitPageClearedEventToObserver()
    }

    private fun isNotFiltered() = model.text.isEmpty()

    private fun isNextPageWillFirst() = model.photoList.isEmpty()

    private fun emitIsAlertFired(text: String) {
        isAlertFiredLiveData.value = text
    }

    private fun emitIsRefreshing(isRefreshing: Boolean) {
        isRefreshingLiveData.value = isRefreshing
    }

    private fun emitPhotoListUpdatedToObserver() {
        photoListUpdatedLiveData.value = model.photoList
    }

    private fun emitPageClearedEventToObserver() {
        pageClearedLiveData.trigger()
    }

    companion object {
        const val defaultText = "ship"
        const val defaultPerPage = 40
        const val defaultCurrentPage = 0
        const val defaultPhotoPages = 1
        const val defaultIsRefreshing = false
        val defaultPhotoList = ArrayList<MainPhotoModel>()
    }
}