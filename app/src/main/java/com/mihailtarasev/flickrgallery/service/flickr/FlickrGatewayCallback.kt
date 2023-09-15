package com.mihailtarasev.flickrgallery.service.flickr

import com.mihailtarasev.flickrlib.flickr.model.FlickrResponseModel

interface FlickrGatewayCallback {
    fun onSuccess(model: FlickrResponseModel)
    fun onFailure(exception: Exception)
}
