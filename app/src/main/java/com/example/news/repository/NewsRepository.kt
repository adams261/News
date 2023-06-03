package com.example.news.repository

import com.example.news.api.RetrofitInstance
import com.example.news.db.ArticleDatabase
import com.example.news.models.Article

/**
 * Holds the basic methods for retrieving news from the API
 * @author - Philipp Lackner - https://github.com/philipplackner/MVVMNewsApp/
 *
 * @property db
 */
class NewsRepository(
    val db: ArticleDatabase
) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    suspend fun getSportsNews(category: String, pageNumber: Int) =
        RetrofitInstance.api.getSportsNews(category, pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

}
