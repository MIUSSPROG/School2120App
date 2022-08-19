package com.example.school2120app.core.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.view.View
import android.widget.TextView
import com.example.school2120app.R
import com.example.school2120app.data.xlsx.ScheduleParser
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Files.delete
import java.nio.file.Paths

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(val isLoading: Boolean = true) : Resource<T>(null)
}

sealed class UIEvent{
    data class ShowSnackbar(val message: String): UIEvent()
}

sealed class NewsListEvent {
    object Refresh : NewsListEvent()
    data class OnSearchQueryChange(val query: String) : NewsListEvent()
}

interface ActionListener<in T> {
    fun onItemClicked(item: T, view: View? = null)
}

const val SIGNED_DOC = "Подписание документа"
const val UNSIGNED_DOC = "Отказ от подписания документа"

class FileCaching{
    companion object {
        fun save(stream: InputStream, filePrefix: String): String {
            val cachePathStr = "/data/data/com.example.school2120app/cache"
            val cacheDir = File(ScheduleParser.cachePath)
            // если кэш директория не пустая, находим нужный кэш и пересоздаем
            if (cacheDir.listFiles().isNotEmpty()) {
                cacheDir.walk().forEach {
                    val splitPath = it.toString().split('/')
                    if (splitPath[splitPath.size - 1].startsWith(filePrefix)) {
                        delete(it.toPath())
                        return getFilePath(cachePathStr = cachePathStr, filePrefix = filePrefix, stream = stream)
                    }
                }
            }
            // если кэш директория пустая то создаем в ней файл
            return getFilePath(cachePathStr = cachePathStr, filePrefix = filePrefix, stream = stream)
        }

        private fun getFilePath(cachePathStr: String, filePrefix: String, stream: InputStream): String {
            val cachePath = Paths.get(cachePathStr)
            val filePath = Files.createTempFile(cachePath, filePrefix, ".xlsx")
            Files.write(filePath, stream.readBytes()).toString()
            return filePath.toString()
        }

    }
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