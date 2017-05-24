package com.example.tse.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.NetworkImageView
import com.android.volley.toolbox.Volley

/**
 * Created by TSE on 24/5/2017.
 */
class ImageRequester private constructor(context: Context) {

    private val requestQueue: RequestQueue
    private val imageLoader: ImageLoader
    private val maxByteSize: Int

    init {
        this.requestQueue = Volley.newRequestQueue(context.applicationContext)
        this.requestQueue.start()
        this.maxByteSize = calculateMaxByteSize(context)
        this.imageLoader = ImageLoader(
                requestQueue,
                object : ImageLoader.ImageCache {
                    private val lruCache = object : LruCache<String, Bitmap>(maxByteSize) {
                        override fun sizeOf(url: String, bitmap: Bitmap): Int {
                            return bitmap.byteCount
                        }
                    }

                    @Synchronized override fun getBitmap(url: String): Bitmap? {
                        return  lruCache.get(url)
                    }

                    @Synchronized override fun putBitmap(url: String, bitmap: Bitmap) {
                        lruCache.put(url, bitmap)
                    }
                }
        )
    }

    fun setImageFromUrl(networkImageView: NetworkImageView, url: String){
        networkImageView.setImageUrl(url, imageLoader)
    }

    companion object {
        @Volatile private var instance: ImageRequester? = null

        private fun calculateMaxByteSize(context: Context): Int {
            val displayMetrics = context.resources.displayMetrics
            val screenBytes = displayMetrics.widthPixels * displayMetrics.heightPixels * 4
            return screenBytes * 3
        }


        fun getInstance(context: Context): ImageRequester {
            var result = instance
            if (result == null) {
                synchronized(ImageRequester::class.java){
                    result = instance
                    if (result == null) {
                        instance = ImageRequester(context)
                        result = instance
                    }
                }
            }
            return result!!
        }
    }
}