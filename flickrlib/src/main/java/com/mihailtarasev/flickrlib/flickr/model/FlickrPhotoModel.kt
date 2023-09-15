package com.mihailtarasev.flickrlib.flickr.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FlickrPhotoModel(
    @SerializedName("id")
    @Expose val id: String,
    @SerializedName("title")
    @Expose val title: String,
    @SerializedName("url_s")
    @Expose val smallImageUrl: String?,
    @SerializedName("url_l")
    @Expose val largeImageUrl: String?
)