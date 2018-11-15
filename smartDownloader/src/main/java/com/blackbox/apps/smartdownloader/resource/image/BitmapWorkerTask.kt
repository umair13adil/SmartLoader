package com.blackbox.apps.smartdownloader.resource.image

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.webkit.URLUtil
import android.widget.ImageView
import com.blackbox.apps.smartdownloader.cache.MemoryCache.addBitmapToMemoryCache
import com.blackbox.apps.smartdownloader.resource.ResourceCallback
import com.blackbox.apps.smartdownloader.utils.createBitmapFromFile
import com.blackbox.apps.smartdownloader.utils.createBitmapFromResource
import com.blackbox.apps.smartdownloader.utils.createBitmapFromResponse
import okhttp3.Response
import java.lang.ref.WeakReference

internal class BitmapWorkerTask(private val path: String?, private val resourceId: Int?, imageView: ImageView, private val context: Context) : AsyncTask<Response, Void, Bitmap>() {

    private var callback: ResourceCallback? = null

    fun setListener(resourceCallback: ResourceCallback) {
        this.callback = resourceCallback
    }

    private val imageViewReference: WeakReference<ImageView>?

    init {
        //Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = WeakReference(imageView)
    }

    override fun doInBackground(vararg params: Response): Bitmap? {
        var bitmap: Bitmap? = null

        //Create bitmap from network response
        path?.let {
            val isURL = (URLUtil.isHttpUrl(path) || URLUtil.isHttpsUrl(path))

            bitmap = if (isURL) {
                createBitmapFromResponse(params[0])
            } else {
                createBitmapFromFile(it)
            }

            addBitmapToMemoryCache(it, bitmap)
        }

        resourceId?.let {
            bitmap = createBitmapFromResource(it, context)
            addBitmapToMemoryCache(it.toString(), bitmap)
        }

        return bitmap
    }

    //onPostExecute() sets the bitmap fetched by doInBackground()
    override fun onPostExecute(bitmap: Bitmap?) {
        if (imageViewReference != null && bitmap != null) {
            val imageView = imageViewReference.get() as ImageView
            imageView.setImageBitmap(bitmap)
            callback?.onLoaded(bitmap)
        }else{
            callback?.onLoadFailed(Exception("Unable to load image!"))
        }
    }
}