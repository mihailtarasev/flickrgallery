package com.mihailtarasev.flickrlib.flickr.client

import com.mihailtarasev.flickrlib.flickr.model.FlickrResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

internal interface FlickrApiRetrofitService {
    @GET("?method=flickr.photos.search&extras=url_s,url_l%2C&format=json&nojsoncallback=1")
    fun getPhotosList(
        @Query("api_key") api_key: String,
        @Query("text") text: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
    ): Call<FlickrResponseModel>
}