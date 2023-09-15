package com.mihailtarasev.flickrgallery.scene.main.model

import com.mihailtarasev.flickrlib.flickr.model.FlickrResponseModel

data class MainNetworkModel(
    var photoPageNumber: Int,
    var photoPages: Int,
    var photoList: ArrayList<MainUseCasePhotoModel>
) {
    companion object {
        fun create(model: FlickrResponseModel): MainNetworkModel {
            val flickrPhotoList: List<MainUseCasePhotoModel> = model.photos.photoList.mapNotNull {
                if (!it.smallImageUrl.isNullOrEmpty() && !it.largeImageUrl.isNullOrEmpty()) {
                    MainUseCasePhotoModel(it.id, it.title, it.smallImageUrl!!, it.largeImageUrl!!)
                } else {
                    null
                }
            }
            val flickrPhotosPage = model.photos.page.toInt()
            val flickrPhotosPages = model.photos.pages.toInt()
            return MainNetworkModel(flickrPhotosPage, flickrPhotosPages, ArrayList(flickrPhotoList))
        }
    }
}
