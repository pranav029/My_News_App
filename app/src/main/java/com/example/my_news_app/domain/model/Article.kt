package com.example.my_news_app.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.IgnoredOnParcel

@Parcelize
data class Article(
    val author: String?,
    val description: String?,
    val title: String?,
    val url: String?,
    val urlToImage: String?,
    val content: String?,
    val source: String?,
    val time: String?,
    @IgnoredOnParcel
    val isFavVisible: Boolean = true,
    @IgnoredOnParcel
    val isDeleteVisible: Boolean = false
) : Parcelable