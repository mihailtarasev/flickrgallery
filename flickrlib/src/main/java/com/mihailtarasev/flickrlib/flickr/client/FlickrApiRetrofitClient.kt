package com.mihailtarasev.flickrlib.flickr.client

import com.google.gson.GsonBuilder
import com.mihailtarasev.flickrlib.flickr.model.FlickrResponseModel
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FlickrApiRetrofitClient(private val baseUrl: String, private val apiKey: String) {
    private val serviceClass = FlickrApiRetrofitService::class.java
    private var gson = GsonBuilder()
        .setLenient()
        .create()
    private var apiService: FlickrApiRetrofitService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(serviceClass)

    fun getPhotosSearchRequest(text: String, perPage: Int, page: Int, callback: Callback<FlickrResponseModel>) {
        apiService.getPhotosList(apiKey, text, perPage, page).enqueue(callback)
    }
}