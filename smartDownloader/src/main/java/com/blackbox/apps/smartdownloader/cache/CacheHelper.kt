package com.blackbox.apps.smartdownloader.cache

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.JELLY_BEAN_MR2
import android.os.StatFs
import com.blackbox.apps.smartdownloader.configurations.Configuration
import okhttp3.Cache
import java.io.File


object CacheHelper {

    private val SMART_CACHE = "smart_cache"
    private val MIN_DISK_CACHE_SIZE = 5 * 1024 * 1024 // 5MB
    private val MAX_DISK_CACHE_SIZE = 50 * 1024 * 1024 // 50MB

    private fun createDefaultCacheDir(context: Context): File {
        val cache = File(context.applicationContext.cacheDir, SMART_CACHE)
        if (!cache.exists()) {
            cache.mkdirs()
        }
        return cache
    }

    private fun calculateDiskCacheSize(dir: File): Long {
        var size = MIN_DISK_CACHE_SIZE

        try {
            val statFs = StatFs(dir.absolutePath)

            val blockCount = if (SDK_INT < JELLY_BEAN_MR2) statFs.blockCount.toLong() else statFs.blockCountLong

            val blockSize = if (SDK_INT < JELLY_BEAN_MR2) statFs.blockSize.toLong() else statFs.blockSizeLong
            val available = blockCount * blockSize
            // Target 2% of the total space.
            size = (available / 50).toInt()
        } catch (ignored: IllegalArgumentException) {
        }

        // Bound inside min/max size for disk cache.
        return Math.max(Math.min(size, MAX_DISK_CACHE_SIZE), MIN_DISK_CACHE_SIZE).toLong()
    }

    fun calculateMaxMemoryCacheSize(context: Context): Int {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

        // Use 1/8th of the available memory for this memory cache.
        val cacheSize = maxMemory / 8
        return cacheSize
    }

    fun setUpCache(context: Context, configuration: Configuration): Cache {
        val cacheDirectory = createDefaultCacheDir(context)

        val maxSize = calculateDiskCacheSize(cacheDirectory)
        var cacheSize = configuration.cacheSize

        val cache: Cache
        cache = if (cacheSize < maxSize) {

            val sizeAvailable = maxSize - cacheSize
            cacheSize = (cacheSize + sizeAvailable)

            Cache(cacheDirectory, cacheSize)
        } else
            Cache(cacheDirectory, maxSize)

        return cache
    }

}