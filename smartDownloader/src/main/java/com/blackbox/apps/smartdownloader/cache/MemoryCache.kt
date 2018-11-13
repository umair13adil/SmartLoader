package com.blackbox.apps.smartdownloader.cache

import android.content.Context
import android.graphics.Bitmap
import androidx.collection.LruCache
import java.io.IOException


object MemoryCache {

    private lateinit var mMemoryCache: LruCache<String, Bitmap>

    /**
     * This will setup memory cache according to max available limit on storage directory.
     *
     * @param context
     */
    fun setUpMemoryCache(context: Context) {

        val cacheSizeMax = CacheHelper.calculateMaxMemoryCacheSize(context)

        mMemoryCache = object : LruCache<String, Bitmap>(cacheSizeMax) {

            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.byteCount / 1024
            }
        }
    }

    /**
     * This will setup memory cache according to provided size.
     *
     * @param size size of Cache
     */
    fun setUpMemoryCache(size: Int): LruCache<String, Bitmap> {

        val cacheSizeMax = if (size > 0) {
            size
        } else {
            1
        }

        mMemoryCache = object : LruCache<String, Bitmap>(cacheSizeMax) {

            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                return bitmap.byteCount
            }
        }

        return mMemoryCache
    }

    /**
     * This will add bitmap to In-Memory cache.
     *
     * @param bitmap image resource
     * @param key 'URL' of resource
     */
    fun addBitmapToMemoryCache(key: String, bitmap: Bitmap?) {
        if (getBitmapFromMemCache(key) == null) {
            if (bitmap != null)
                mMemoryCache.put(key, bitmap)
        }
    }

    /**
     * This will return image from cache.
     *
     * @param key 'URL' of resource
     */
    fun getBitmapFromMemCache(key: String): Bitmap? {
        return mMemoryCache.get(key)
    }

    /**
     * This will remove all cache entries.
     */
    fun clearCache() {
        try {
            mMemoryCache.evictAll()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}