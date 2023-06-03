package com.example.news.models

/**
 * Handles the response from search queries
 * @author - Philipp Lackner - https://github.com/philipplackner/MVVMNewsApp/
 *
 * @property articles
 * @property status
 * @property totalResults
 */
data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)