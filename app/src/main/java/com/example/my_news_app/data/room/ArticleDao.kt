package com.example.my_news_app.data.room

import androidx.room.*
import com.example.my_news_app.data.room.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(articleEntity: ArticleEntity):Long

    /*
    * Id must be assigned to entity while using
    * this annotation other wise manual query is needed
    * */
    @Delete
    suspend fun deleteArticle(articleEntity: ArticleEntity): Int

    @Query("SELECT * FROM articleentity")
    fun getAllArticles(): Flow<List<ArticleEntity>>
}