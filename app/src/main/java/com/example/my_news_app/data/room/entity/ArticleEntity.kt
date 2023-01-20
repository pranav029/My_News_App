package com.example.my_news_app.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.my_news_app.domain.model.Article

@Entity
data class ArticleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val author: String?,
    val description: String?,
    val title: String?,
    val url: String?,
    val urlToImage: String?,
    val content: String?,
    val source: String?,
    val time: String?
) {
    fun toArticle(): Article = Article(
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