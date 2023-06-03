package com.example.news.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.adapters.NewsAdapter
import com.example.news.ui.NewsActivity
import com.example.news.ui.NewsViewModel
import com.example.news.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.news.util.Resource
import kotlinx.android.synthetic.main.fragment_sports_news.*

/**
 * Fragment for sports news
 * Slightly altered form of - https://github.com/philipplackner/MVVMNewsApp/
 */
class SportsNewsFragment: Fragment(R.layout.fragment_sports_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    /**
     * Handles the fragment when it is created and being viewed
     *
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_sportsFragment_to_articleFragment, bundle
            )
        }

        viewModel.sportsNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.sportsNewsPage == totalPages
                        if (isLastPage) {
                            rvSportsNews.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    /**
     * hides the progress bar
     *
     */
    private fun hideProgressBar(){
        paginationProgressBar_sports.visibility = View.INVISIBLE
        isLoading = false
    }

    /**
     * shows the progress bar
     */
    private fun showProgressBar(){
        paginationProgressBar_sports.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        /**
         * Handles things when the user is scrolling
         *
         * @param recyclerView
         * @param newState
         */
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        /**
         * Handles things further when the user is scrolling
         *
         * @param recyclerView
         * @param dx
         * @param dy
         */
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if (shouldPaginate){
                viewModel.getSportsNews("sports")
                isScrolling = false
            }
        }
    }

    /**
     * Generates a recycler view.
     *
     */
    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        rvSportsNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SportsNewsFragment.scrollListener)
        }
    }
}


