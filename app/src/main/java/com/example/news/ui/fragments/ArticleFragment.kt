package com.example.news.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.news.R
import com.example.news.ui.NewsActivity
import com.example.news.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_article.*

/**
 * Creates a fragment in which an article can be displayed
 * @author - Philipp Lackner - https://github.com/philipplackner/MVVMNewsApp/
 */
class ArticleFragment: Fragment(R.layout.fragment_article) {
    lateinit var viewModel: NewsViewModel
    val args: ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        val article = args.article
        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }

        //When the floating action button is clicked, the icon changes from a star border to a
        //filled star
        fab.setImageResource(R.drawable.ic_baseline_star_border_24)
        fab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
            fab.setImageResource(R.drawable.ic_baseline_star_24)
        }
    }

}
