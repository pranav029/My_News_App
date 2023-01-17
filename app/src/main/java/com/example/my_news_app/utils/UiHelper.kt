package com.example.my_news_app.utils

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doOnTextChanged
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.my_news_app.R
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import kotlin.math.abs

class UiHelper {
    companion object {
        fun Activity.getInputMethodManager(): InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        fun View.showKeyBoard(activity: Activity) =
            activity.getInputMethodManager().showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)

        fun View.hideKeyBoard(activity: Activity) =
            activity.getInputMethodManager().hideSoftInputFromWindow(this.windowToken, 0)


        fun Context.loadImageFromUrl(
            url: String,
            imageView: ImageView,
            onLoadSuccess: (() -> Unit)? = null
        ) =
            Glide.with(this)
                .asDrawable()
                .load(url)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean = false

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        if (onLoadSuccess != null) {
                            onLoadSuccess()
                        }
                        return false
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.mrvsmk2pl3l8fwocbfhy).into(imageView)

        fun ViewPager2.addCustomTransformer() {
            val transformer = CompositePageTransformer()
            transformer.addTransformer(MarginPageTransformer(40))
            transformer.addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = 0.85f + r * 0.14f
            }
            setPageTransformer(transformer)
        }

        fun EditText.onTextChange(): Flow<CharSequence?> = callbackFlow<CharSequence?> {
            val listener = doOnTextChanged { text, start, before, count ->
                trySend(text)
            }
            awaitClose { removeTextChangedListener(listener) }
        }.onStart { emit(text) }
    }
}