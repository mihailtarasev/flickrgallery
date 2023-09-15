package com.mihailtarasev.flickrgallery.service.flickr

import com.mihailtarasev.flickrlib.flickr.client.FlickrApiRetrofitClient
import com.mihailtarasev.flickrlib.flickr.model.FlickrResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FlickrRetrofitGateway(var apiClient: FlickrApiRetrofitClient) {

    fun getPhotosSearchRequest(text: String, perPage: Int, page: Int, callback: FlickrGatewayCallback) {
        apiClient.getPhotosSearchRequest(text, perPage, page, object : Callback<FlickrResponseModel> {
            override fun onResponse(call: Call<FlickrResponseModel>, response: Response<FlickrResponseModel>) {
                if (response.isSuccessful) {
                    val responseModel = response.body()
                    if (responseModel != null) {
                        callback.onSuccess(responseModel)
                    }
                }
            }

            override fun onFailure(call: Call<FlickrResponseModel>, t: Throwable) {
                val exception = Exception(t)
                callback.onFailure(exception)
            }
        })
    }

    companion object {
        fun create(baseUrl: String, apiKey: String): FlickrRetrofitGateway {
            val flickrApiClient = FlickrApiRetrofitClient(baseUrl, apiKey)
            return FlickrRetrofitGateway(flickrApiClient)
        }
    }
}

suspend fun FlickrRetrofitGateway.awaitGetPhotosSearchRequest(text: String, perPage: Int, page: Int): FlickrResponseModel = suspendCoroutine {
    getPhotosSearchRequest(text, perPage, page, object: FlickrGatewayCallback {
        override fun onSuccess(model: FlickrResponseModel) {  it.resume(model)  }

        override fun onFailure(exception: Exception) { it.resumeWithException(exception) }
    })
}