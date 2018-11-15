package com.blackbox.apps.smartdownloader.resource.image

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.webkit.URLUtil
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.blackbox.apps.smartdownloader.cache.MemoryCache
import com.blackbox.apps.smartdownloader.resource.ResourceCallback
import okhttp3.*
import java.io.IOException

/**
 * This will load image either from cache or by downloading image from server.
 */
object ImageLoader {

    fun loadImage(path: String?, resourceId: Int?, context: Context, imageView: ImageView, okHttpClient: OkHttpClient, imageCallback: ResourceCallback) {

        path?.let {
            val cachedBitmap = MemoryCache.getBitmapFromMemCache(path)

            //If there is no cached image, then download one
            if (cachedBitmap != null) {

                //Draw Cached image
                imageCallback.onLoaded(cachedBitmap)

            } else {

                if (path.isNotEmpty()) {

                    //If image is not found in cache then download it
                    downloadImage(path, imageView, context, okHttpClient, imageCallback)
                } else {
                    throw Exception("Empty path provided!")
                }
            }
        }

        resourceId?.let {
            val cachedBitmap = MemoryCache.getBitmapFromMemCache(resourceId.toString())

            //If there is no cached image, then download one
            if (cachedBitmap != null) {

                //Draw Cached image
                imageCallback.onLoaded(cachedBitmap)

            } else {
                //Create bitmap from resource Id, add bitmap to cache and view it on ImageView
                val task = BitmapWorkerTask(null, resourceId, imageView, context)
                task.setListener(imageCallback)
                task.execute()
            }
        }
    }

    private fun downloadImage(path: String, imageView: ImageView, context: Context, okHttpClient: OkHttpClient, imageCallback: ResourceCallback) {

        val isURL = (URLUtil.isHttpUrl(path) || URLUtil.isHttpsUrl(path))

        //Execute if provided path is of 'http' or 'https'
        if (isURL) {

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
                    val task = BitmapWorkerTask(path, null, imageView, context)
                    task.setListener(imageCallback)
                    task.execute(response)
                }
            })
        } else {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Create bitmap from response, add bitmap to cache and view it on ImageView
                val task = BitmapWorkerTask(path, null, imageView, context)
                task.setListener(imageCallback)
                task.execute()
            } else {
                throw Exception("Storage read permission not granted!")
            }
        }
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