package com.mihailtarasev.flickrgallery.service.flickr

import com.mihailtarasev.flickrlib.flickr.client.FlickrApiClientImp
import com.mihailtarasev.flickrlib.flickr.model.FlickrResponseModel

class FlickrGateway(private var apiClient: FlickrApiClientImp) {

    fun awaitGetPhotosSearchRequest(text: String, perPage: Int, page: Int): FlickrResponseModel {
        return apiClient.getPhotosSearchRequest(text, perPage, page)
    }

    companion object {
        fun create(baseUrl: String, apiKey: String): FlickrGateway {
            val flickrApiClient = FlickrApiClientImp(baseUrl, apiKey)
            return FlickrGateway(flickrApiClient)
        }
    }
}