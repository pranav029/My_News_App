package com.example.my_news_app.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.my_news_app.data.room.entity.ArticleEntity

@Database(
    entities = [ArticleEntity::class],
    version = 1
)
abstract class ArticleDatabase:RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}