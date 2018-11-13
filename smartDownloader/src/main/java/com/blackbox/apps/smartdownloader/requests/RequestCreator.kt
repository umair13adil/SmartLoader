package com.blackbox.apps.smartdownloader.requests

import android.graphics.Bitmap
import android.widget.ImageView
import com.blackbox.apps.smartdownloader.cache.MemoryCache
import com.blackbox.apps.smartdownloader.resource.ResourceCallback
import com.blackbox.apps.smartdownloader.configurations.Configuration
import com.blackbox.apps.smartdownloader.resource.image.ImageLoader
import com.blackbox.apps.smartdownloader.resource.Resource


class RequestCreator(configuration: Configuration) {

    private var mConfiguration: Configuration? = null

    companion object {

        @Volatile
        private var INSTANCE: RequestCreator? = null

        fun getInstance(configuration: Configuration) =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                            ?: RequestCreator(configuration).also {
                                it.setConfiguration(configuration)
                                INSTANCE = it
                            }
                }
    }

    private fun setConfiguration(configuration: Configuration) {

        if (mConfiguration == null)
            this.mConfiguration = configuration
    }

    /**
     * This will create request for loading image.
     *
     * @param imageLoader
     */
    fun createImageLoadRequest(resource: Resource) {

        val context = resource.context?.applicationContext
                ?: throw Exception("Null context provided!")

        //Setup OkHttp Client
        val okHttpClient = ClientHelper.createClient(mConfiguration!!, context)

        //Setup InMemory cache
        MemoryCache.setUpMemoryCache(context)

        if (resource.url == null)
            throw Exception("URL can not be null!")

        if (resource.view == null)
            throw Exception("ImageView can not be null!")

        if (resource.view !is ImageView)
            throw Exception("Provided view is not of type ImageView!")

        if (resource.url!!.isEmpty())
            throw Exception("URL can not be empty!")

        val callBack = object : ResourceCallback {
            override fun onCachedLoaded(bitmap: Bitmap) {

                resource.view?.let {
                    ImageLoader.renderImage(bitmap, it)
                }
            }

            override fun onLoadFailed(e: Exception) {
                e.printStackTrace()
            }
        }

        resource.let {
            ImageLoader.loadImage(it.url!!, it.view!!, okHttpClient, callBack)
        }
    }
}