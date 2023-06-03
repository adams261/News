package com.example.news.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.news.models.Article

/**
 * Uses SQLite to query articles from the article database
 * @author - Philipp Lackner - https://github.com/philipplackner/MVVMNewsApp/
 *
 */
@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}