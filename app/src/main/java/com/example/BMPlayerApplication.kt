package com.example

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import com.example.core.database.AppDatabase
import com.example.core.playback.PlaybackController

class BMPlayerApplication : Application(), ImageLoaderFactory {

    private var customImageLoader: ImageLoader? = null

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "BM Player Application initialized")
        
        try {
            AppDatabase.getInstance(this)
            PlaybackController.getInstance(this)
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing core singletons", e)
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    // Restrict in-memory bitmap cache to 10% of available app RAM (Low-RAM optimization)
                    .maxSizePercent(0.10)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("coil_artwork_cache"))
                    .maxSizeBytes(32L * 1024 * 1024) // 32 MB disk limit
                    .build()
            }
            // Enforce RGB_565 (16-bit color, 2 bytes/pixel vs 4 bytes in ARGB_8888) for 50% RAM reduction
            .bitmapConfig(Bitmap.Config.RGB_565)
            .allowRgb565(true)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .respectCacheHeaders(false)
            .crossfade(100)
            .build().also { customImageLoader = it }
    }

    fun handleTrimMemory(level: Int) {
        if (level >= TRIM_MEMORY_RUNNING_LOW || level >= TRIM_MEMORY_BACKGROUND) {
            customImageLoader?.memoryCache?.clear()
        }
    }

    fun handleLowMemory() {
        customImageLoader?.memoryCache?.clear()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        handleTrimMemory(level)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        handleLowMemory()
    }

    companion object {
        private const val TAG = "BMPlayerApp"
    }
}
