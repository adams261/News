package com.example.news.util

/**
 * Used in the creation of success/error messages
 * @author - Philipp Lackner - https://github.com/philipplackner/MVVMNewsApp/
 *
 * @param T
 * @property data
 * @property message
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}