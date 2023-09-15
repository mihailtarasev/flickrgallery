package com.mihailtarasev.flickrlib.flickr.client

import com.google.gson.GsonBuilder
import com.mihailtarasev.flickrlib.flickr.model.FlickrResponseModel

internal class FlickrHttpResponseParser {
    private val gson = GsonBuilder().setPrettyPrinting().create()

    fun parseGetPhotosSearchResponse(httpResponse: String?): FlickrResponseModel {
        return gson.fromJson(notNullResponse(httpResponse), FlickrResponseModel::class.java)
    }

    private fun notNullResponse(httpResponse: String?): String {
        if(httpResponse != null) {
            return httpResponse
        } else {
            throw (Exception(httpResponseParserException))
        }
    }

    companion object {
        const val httpResponseParserException = "HttpResponse is failed"
    }
}