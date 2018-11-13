package com.blackbox.apps.smartdownloader.resource.image

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.blackbox.apps.smartdownloader.cache.MemoryCache
import com.blackbox.apps.smartdownloader.resource.ResourceCallback
import okhttp3.*
import java.io.IOException

/**
 * This will load image either from cache or by downloading image from server.
 */
object ImageLoader {

    fun loadImage(path: String, imageView: ImageView, okHttpClient: OkHttpClient, imageCallback: ResourceCallback) {

        val cachedBitmap = MemoryCache.getBitmapFromMemCache(path)

        //If there is no cached image, then download one
        if (cachedBitmap != null) {
            //Draw Cached image
            imageCallback.onCachedLoaded(cachedBitmap)

        } else {

            //If image is not found in cache then download it
            downloadImage(path, imageView, okHttpClient, imageCallback)
        }
    }

    private fun downloadImage(path: String, imageView: ImageView, okHttpClient: OkHttpClient, imageCallback: ResourceCallback) {

        //Create new download request
        val request = Request.Builder()
                .url(path)
                .tag(path) //Set Resource path as TAG
                .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                imageCallback.onLoadFailed(e)
            }

            override fun onResponse(call: Call, response: Response) {

                if (response.body() == null || response.body()?.byteStream() == null) {
                    imageCallback.onLoadFailed(Exception("null stream, unable to decode image!"))
                }

                //Create bitmap from response, add bitmap to cache and view it on ImageView
                BitmapWorkerTask(path, imageView).execute(response)
            }
        })
    }


    /**
     * This will draw image on main thread.
     */
    fun renderImage(bitmap: Bitmap, view: ImageView) {
        Handler(Looper.getMainLooper()).post {
            view.setImageBitmap(bitmap)
        }
    }
}