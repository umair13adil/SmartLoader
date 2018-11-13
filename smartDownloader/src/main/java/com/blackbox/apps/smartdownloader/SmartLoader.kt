package com.blackbox.apps.smartdownloader

import android.content.Context
import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.blackbox.apps.smartdownloader.cache.MemoryCache
import com.blackbox.apps.smartdownloader.configurations.Configuration
import com.blackbox.apps.smartdownloader.configurations.SmartConfiguration
import com.blackbox.apps.smartdownloader.requests.RequestCreator
import com.blackbox.apps.smartdownloader.requests.RequestDispatcher
import com.blackbox.apps.smartdownloader.resource.Resource

/**
 * SmartLoader will load images and cache them in Memory.
 * This class will also keep track of 'LifeCycle' events.
 */
class SmartLoader : LifecycleObserver {

    private var context: Context? = null
    private var url: String? = null
    private var view: ImageView? = null
    private var configuration: Configuration = SmartConfiguration().build()

    /**
     * Get Context.
     *
     * @param context
     */
    fun withContext(context: Context?) = apply { this.context = context }

    /**
     * URL of resource to be downloaded.
     *
     * @param url of Resource
     */
    fun load(url: String?) = apply { this.url = url }

    /**
     * ImageView on which image will be displayed after downloading.
     *
     * @param view of type ImageView
     */
    fun into(view: ImageView?) = apply { this.view = view }.also {
        val request = build()
        RequestCreator.getInstance(it.configuration).createImageLoadRequest(request)
    }

    /**
     * Default configuration.
     * If configuration is not set then default configuration will be used.
     *
     * @param smartConfiguration Global configuration for smart downloader
     */
    fun initWithConfiguration(smartConfiguration: Configuration) = apply {
        this.configuration = smartConfiguration
        RequestCreator.getInstance(smartConfiguration)
    }


    /**
     * Build resource that is provided.
     */
    private fun build() = Resource(
            context,
            url,
            view,
            configuration
    )

    /**
     * This will clear the cache, calling entryRemoved(boolean, K, V, V) on each removed entry.
     */
    fun resetCache() {
        MemoryCache.clearCache()
    }

    /**
     * This will cancel call for any provided tag.
     * TAG is the 'URL' path of resource.
     */
    fun cancelLoadForTag(tag: Any) {
        RequestDispatcher.getInstance().cancel(tag)
    }

    /**
     * This will cancel all requests that are pending or currently running.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun cancelAllRequests() {
        RequestDispatcher.getInstance().cancelAll()
    }
}