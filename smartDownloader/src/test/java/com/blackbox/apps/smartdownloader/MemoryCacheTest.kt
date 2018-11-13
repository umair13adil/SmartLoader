package com.blackbox.apps.smartdownloader

import android.graphics.Bitmap
import com.blackbox.apps.smartdownloader.cache.MemoryCache
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class MemoryCacheTest {

    private val A = Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8)
    private val B = Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8)
    private val C = Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8)

    @Test
    fun testCachePutSize() {
        var expectedPutCount = 0

        val cache = MemoryCache.setUpMemoryCache(3)

        cache.put("a", A)
        expectedPutCount++

        cache.put("b", B)
        expectedPutCount++

        cache.put("c", C)
        expectedPutCount++

        assertThat(cache.putCount()).isEqualTo(expectedPutCount)
    }

    @Test
    fun testCacheExists() {
        val cache = MemoryCache.setUpMemoryCache(1)
        cache.put("a", A)
        assertThat(cache.get("b")).isNull()
    }

    @Test
    fun evictAll() {
        val cache = MemoryCache.setUpMemoryCache(3)
        cache.put("a", A)
        cache.put("b", B)
        cache.put("c", C)
        cache.evictAll()
        assertThat(cache.createCount()).isEqualTo(0)
    }
}