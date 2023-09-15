package com.mihailtarasev.flickrgallery.service.flickr.gateway

import com.mihailtarasev.flickrgallery.scene.main.usecase.MainNetworkGatewayImp
import com.mihailtarasev.flickrgallery.service.flickr.FlickrGateway
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class FlickrGatewayTest {
    private val flickrGateway = FlickrGateway.create(MainNetworkGatewayImp.baseUrl, MainNetworkGatewayImp.apiKey)

    @Test
    fun testExecute() = runTest {
        val text = "ship"
        val perPage = 10
        val page = 1
        try {
            val result = flickrGateway.awaitGetPhotosSearchRequest(text, perPage, page)
            assert(result.photos.page.toInt() == page)
            assert(result.photos.photoList.size == perPage)
        }catch (exception: Exception) {
            assert(false)
        }
    }
}

