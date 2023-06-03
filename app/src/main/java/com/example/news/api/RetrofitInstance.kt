package com.example.news.api

import com.example.news.util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Builds a URL to get articles from using the API
 * @author - Philipp Lackner - https://github.com/philipplackner/MVVMNewsApp/
 *
 */
class RetrofitInstance {
    companion object{
        private val retrofit by lazy{
            val logging = HttpLoggingInterceptor()
            val level = logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val api by lazy{
            retrofit.create(newsAPI::class.java)
        }
    }
}