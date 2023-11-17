package com.mihailtarasev.flickrgallery.scene.details

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mihailtarasev.flickrgallery.databinding.ActivityDetailsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

class DetailsActivity: AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var screenImageUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configure()
        getIntentExtra()
        displayImage()
    }
    private fun configure() {
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    private fun getIntentExtra() {
        screenImageUrl = intent.getStringExtra(extraScreenImageUrl) ?: defaultScreenImageUrl
    }
    private fun displayImage() {
        lifecycleScope.launch(Dispatchers.IO) {
            val stream = URL(screenImageUrl).openStream()
            val image = BitmapFactory.decodeStream(stream)
            runOnUiThread {
                binding.screenImageView.setImageBitmap(image)
            }
        }
    }
    companion object {
        const val extraScreenImageUrl = "ExtraDetailsActivityScreenImageUrl"
        const val defaultScreenImageUrl = "https://loremflickr.com/320/240"
    }
}