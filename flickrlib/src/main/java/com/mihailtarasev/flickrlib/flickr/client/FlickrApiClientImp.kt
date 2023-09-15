package com.mihailtarasev.flickrlib.flickr.client

import com.mihailtarasev.flickrlib.flickr.model.FlickrResponseModel
import com.mihailtarasev.flickrlib.http.HttpRequestService

class FlickrApiClientImp(private var baseUrl: String, private var apiKey: String) {
    private val httpRequestService = HttpRequestService()
    private val parser = FlickrHttpResponseParser()

    fun getPhotosSearchRequest(text: String, perPage: Int, page: Int): FlickrResponseModel {
        val immutablePart = "method=flickr.photos.search&extras=url_s,url_l%2C&format=json&nojsoncallback=1"
        val resultParams = "${immutablePart}&api_key=${apiKey}&text=${text}&per_page=${perPage}&page=${page}"
        val response = httpRequestService.request(baseUrl, resultParams)
        return parser.parseGetPhotosSearchResponse(response)
    }
}