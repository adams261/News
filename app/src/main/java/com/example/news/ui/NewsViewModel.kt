package com.example.news.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.NewsApplication
import com.example.news.models.Article
import com.example.news.models.NewsResponse
import com.example.news.repository.NewsRepository
import com.example.news.util.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

/**
 * Sets up functionality for safely getting the news without crashing the app
 * @author - Philipp Lackner - https://github.com/philipplackner/MVVMNewsApp/
 *
 * @property newsRepository
 * @constructor
 * TODO
 *
 * @param app
 */

class NewsViewModel(
    app: Application,
    val newsRepository : NewsRepository
) : AndroidViewModel(app) {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse : NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse : NewsResponse? = null

    val sportsNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var sportsNewsPage = 1
    var sportsNewsResponse : NewsResponse? = null

    init {
        getBreakingNews("gb")
        getSportsNews("sports")
    }

    /**
     * Uses a safe call to get breaking news
     *
     * @param countryCode
     */
    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode)
    }

    /**
     * Uses a safe call to get searched news
     *
     * @param countryCode
     */
    fun searchNews(searchQuery: String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }

    /**
     * Uses a safe call to get sports news
     *
     * @param category
     */
    fun getSportsNews(category: String) = viewModelScope.launch {
        safeSportsNewsCall(category)
    }

    /**
     * Generates a news response (list) of breaking news articles
     *
     * @param response
     * @return
     */
    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    /**
     * Generates a news response (list) of searched news articles
     *
     * @param response
     * @return
     */
    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                searchNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    /**
     * Generates a news response (list) of sports news articles
     *
     * @param response
     * @return
     */
    private fun handleSportsNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                sportsNewsPage++
                if (sportsNewsResponse == null) {
                    sportsNewsResponse = resultResponse
                } else {
                    val oldArticles = sportsNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(sportsNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    /**
     * Saves an article.
     *
     * @param article
     */
    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    /**
     * Gets the saved articles.
     *
     */
    fun getSavedNews() = newsRepository.getSavedNews()

    /**
     * Deletes the saved article
     *
     * @param article
     */
    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    /**
     * Safe call for searched news
     *
     * @param searchQuery
     */
    private suspend fun safeSearchNewsCall(searchQuery: String){
        searchNews.postValue(Resource.Loading())
        try{
            if (hasInternetConnection()){
                val response = newsRepository.searchNews(searchQuery, searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                searchNews.postValue(Resource.Error("Internet connection not detected"))
            }

        } catch(t: Throwable){
            when(t){
                is IOException -> searchNews.postValue(Resource.Error("Network failure"))
                else -> searchNews.postValue(Resource.Error("Conversion error"))
            }
        }
    }

    /**
     * Safe call for breaking news
     *
     * @param countryCode
     */
    private suspend fun safeBreakingNewsCall(countryCode: String){
        breakingNews.postValue(Resource.Loading())
        try{
            if (hasInternetConnection()){
                val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            } else {
                breakingNews.postValue(Resource.Error("Internet connection not detected"))
            }

        } catch(t: Throwable){
            when(t){
                is IOException -> breakingNews.postValue(Resource.Error("Network failure"))
                else -> breakingNews.postValue(Resource.Error("Conversion error"))
            }
        }
    }

    /**
     * Safe call for sports news
     *
     * @param category
     */
    private suspend fun safeSportsNewsCall(category: String){
        sportsNews.postValue(Resource.Loading())
        try{
            if (hasInternetConnection()){
                val response = newsRepository.getSportsNews(category, sportsNewsPage)
                sportsNews.postValue(handleSportsNewsResponse(response))
            } else {
                sportsNews.postValue(Resource.Error("Internet connection not detected"))
            }

        } catch(t: Throwable){
            when(t){
                is IOException -> sportsNews.postValue(Resource.Error("Network failure"))
                else -> sportsNews.postValue(Resource.Error("Conversion error"))
            }
        }
    }

    /**
     * Checks the user has an active internet connection
     *
     * @return
     */
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run{
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }


}