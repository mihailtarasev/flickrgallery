package com.mihailtarasev.flickrgallery.scene.main.router

import android.content.Context
import android.content.Intent
import com.mihailtarasev.flickrgallery.scene.details.DetailsActivity

class MainRouter(var context: Context) {
    fun startDetailsActivity(url: String) {
        val intent = Intent(context, DetailsActivity::class.java)
        intent.putExtra(DetailsActivity.extraScreenImageUrl, url)
        context.startActivity(intent)
    }
}