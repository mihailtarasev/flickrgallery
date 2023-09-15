package com.mihailtarasev.flickrlib.flickr.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FlickrPhotosModel(
    @SerializedName("page")
    @Expose
    val page: String,
    @SerializedName("pages")
    @Expose
    val pages: String,
    @SerializedName("photo")
    @Expose
    val photoList: ArrayList<FlickrPhotoModel>
)