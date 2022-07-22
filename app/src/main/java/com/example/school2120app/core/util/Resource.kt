package com.example.school2120app.core.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.widget.TextView
import com.example.school2120app.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(val isLoading: Boolean = true) : Resource<T>(null)
}

sealed class NewsListEvent {
    object Refresh : NewsListEvent()
    data class OnSearchQueryChange(val query: String) : NewsListEvent()
}

interface ActionListener<in T> {
    fun itemClick(item: T)
}

class PicassoImageGetter(
    private val textView: TextView,
    private val context: Context
) : Html.ImageGetter {

    override fun getDrawable(source: String?): Drawable {
        val drawable = BitmapDrawablePlaceHolder()
        val displayMetrics = context.resources.displayMetrics
        val windowWidth = (displayMetrics.widthPixels / displayMetrics.density).toInt()
        val windowHeight = (displayMetrics.heightPixels / displayMetrics.density).toInt()
        Picasso
            .get()
            .load(source)
            .resize(windowWidth, windowHeight)
            .centerCrop()
            .placeholder(R.drawable.ic_image_loading)
            .into(drawable)
        return drawable
    }

    inner class BitmapDrawablePlaceHolder(private var drawable: Drawable? = null) :
        BitmapDrawable(), Target {

        override fun draw(canvas: Canvas) {
            drawable?.draw(canvas)
        }

        fun setDrawable(drawable: Drawable) {
            this.drawable = drawable
            val width = drawable.intrinsicWidth
            val height = drawable.intrinsicHeight
            drawable.setBounds(0, 0, width, height)
            setBounds(0, 0, width, height)
            textView.text = textView.text
        }

        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            setDrawable(BitmapDrawable(context.resources, bitmap))
        }

        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {

        }

        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

        }
    }

}