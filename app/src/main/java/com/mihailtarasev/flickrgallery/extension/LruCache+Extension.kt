package com.mihailtarasev.flickrgallery.extension

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import java.net.URL

fun LruCache<String, Bitmap>.cachedImage(url: String): Bitmap {
    val cache = this.get(url)
    return if(cache != null)  {
        cache
    }else {
        val stream = URL(url).openStream()
        val image = BitmapFactory.decodeStream(stream)
        this.put(url, image)
        image
    }
}