package com.mihailtarasev.flickrgallery.scene.main.usecase

import com.mihailtarasev.flickrgallery.scene.main.model.MainNetworkModel

interface MainNetworkGateway {
    suspend fun getPhotoPageRequest(text: String, perPage: Int, page: Int): MainNetworkModel
}