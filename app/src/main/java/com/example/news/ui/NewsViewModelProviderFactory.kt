package com.example.news.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.news.repository.NewsRepository

/**
 * Creates News viewmodels
 * @author - Philipp Lackner - https://github.com/philipplackner/MVVMNewsApp/
 *
 * @property app
 * @property newsRepository
 */
class NewsViewModelProviderFactory (
    val app: Application,
    val newsRepository: NewsRepository
        ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(app, newsRepository) as T
    }
}