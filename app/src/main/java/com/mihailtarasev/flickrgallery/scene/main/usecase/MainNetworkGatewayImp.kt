package com.mihailtarasev.flickrgallery.scene.main.usecase

import com.mihailtarasev.flickrgallery.scene.main.model.MainNetworkModel
import com.mihailtarasev.flickrgallery.service.flickr.FlickrGateway

class MainNetworkGatewayImp: MainNetworkGateway {
    private val flickrGateway = FlickrGateway.create(baseUrl, apiKey) // or FlickrRetrofitGateway

    override suspend fun getPhotoPageRequest(text: String, perPage: Int, page: Int): MainNetworkModel {
        try {
            val model = flickrGateway.awaitGetPhotosSearchRequest(text, perPage, page)
            return MainNetworkModel.create(model)
        } catch (exception: Exception) {
            throw exception
        }
    }

    companion object {
        const val apiKey = "c149f0804ae20cb6ee453d66debef32f"
        const val baseUrl = "https://www.flickr.com/services/rest/"
    }
}