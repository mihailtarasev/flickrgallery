package com.mihailtarasev.flickrgallery.scene.main.view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mihailtarasev.flickrgallery.R
import com.mihailtarasev.flickrgallery.databinding.ShowListRawBinding
import com.mihailtarasev.flickrgallery.extension.cachedImage
import com.mihailtarasev.flickrgallery.scene.main.model.MainPhotoModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityAdapter(var callback: MainActivityAdapterCallback, var placeholder: Drawable) :
    RecyclerView.Adapter<MainActivityAdapter.MainActivityAdapterHolder>() {
    private var flickrItemList = ArrayList<MainPhotoModel>()
    private val coroutine = CoroutineScope(Dispatchers.IO)
    private val maxMemory = (Runtime.getRuntime().maxMemory() / 1024)
    private val cacheSize = maxMemory / 8;
    private val memoryCache: LruCache<String, Bitmap> = LruCache(cacheSize.toInt())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainActivityAdapterHolder {
        return MainActivityAdapterHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.show_list_raw, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainActivityAdapterHolder, position: Int) {
        setThumbnailPlaceholder(holder)
        setThumbnailImage(holder, position)
        setOnItemClickListener(holder, position)
    }

    private fun setThumbnailPlaceholder(holder: MainActivityAdapterHolder) {
        holder.binding.thumbnail.setImageDrawable(placeholder)
    }

    private fun setThumbnailImage(holder: MainActivityAdapterHolder, position: Int) {
        val smallImageUrl = flickrItemList[position].smallImageUrl
        coroutine.launch {
            val image = memoryCache.cachedImage(smallImageUrl)
            holder.itemView.post {
                holder.binding.thumbnail.setImageBitmap(image)
            }
        }
    }

    private fun setOnItemClickListener(holder: MainActivityAdapterHolder, position: Int) {
        val largeImageUrl = flickrItemList[position].largeImageUrl
        onItemClickListener(holder.itemView, largeImageUrl)
    }

    private fun onItemClickListener(view: View, imageUrl: String) {
        view.setOnClickListener {
            callback.onMainActivityAdapterItemCLick(imageUrl)
        }
    }

    override fun getItemCount(): Int {
        return flickrItemList.size
    }

    class MainActivityAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ShowListRawBinding.bind(itemView)
    }

    fun updateList(view: RecyclerView, flickrItemList: ArrayList<MainPhotoModel>, oldCount: Int, flickrItemListSize: Int) {
        this.flickrItemList = flickrItemList
        view.post {
            notifyItemRangeInserted(oldCount, flickrItemListSize)
        }
    }

    fun clearList() {
        this.flickrItemList = ArrayList()
        notifyItemRangeRemoved(0, 0);
    }
}