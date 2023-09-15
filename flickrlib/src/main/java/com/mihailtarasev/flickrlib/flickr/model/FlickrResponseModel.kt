package com.mihailtarasev.flickrlib.flickr.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FlickrResponseModel(
    @SerializedName("photos")
    @Expose val photos: FlickrPhotosModel,
    @SerializedName("stat")
    @Expose val stat: String,
)