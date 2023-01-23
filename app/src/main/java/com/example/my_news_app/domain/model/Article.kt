package com.example.my_news_app.domain.model

import android.os.Parcelable
import com.example.my_news_app.data.room.entity.ArticleEntity
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.IgnoredOnParcel

@Parcelize
data class Article(
    val id:Long? = null,
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
    val isDeleteVisible: Boolean = false,
    val isFav:Boolean = false
) : Parcelable{
    fun toArticleEntity():ArticleEntity = ArticleEntity(
        id = id,
        author = author,
        description = description,
        title = title,
        url = url,
        urlToImage = urlToImage,
        content = content,
        source = source,
        time = time
    )
}