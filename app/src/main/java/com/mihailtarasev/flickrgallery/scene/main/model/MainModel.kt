package com.mihailtarasev.flickrgallery.scene.main.model

import com.mihailtarasev.flickrgallery.scene.main.usecase.MainViewModel

data class MainModel(
    var text: String,
    var currentPageNumber: Int,
    var photoPages: Int,
    var photoList: ArrayList<MainPhotoModel>,
    var isRefreshing: Boolean
) {
    fun updateBy(result: MainNetworkModel) {
        photoPages = result.photoPages
        photoList.addAll(result.photoList)
        currentPageNumber = result.photoPageNumber
    }

    fun clearPage() {
        photoList.clear()
        currentPageNumber = MainViewModel.defaultCurrentPage
        photoPages = MainViewModel.defaultPhotoPages
    }

    fun isAvailableNextPage(): Boolean {
        return currentPageNumber < photoPages
    }

}