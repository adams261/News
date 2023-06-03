package com.example.news.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.news.R
import com.example.news.db.ArticleDatabase
import com.example.news.repository.NewsRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_news.*

/**
 * Sets up the main activity
 *
 */
class NewsActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    lateinit var toggle: ActionBarDrawerToggle


    /**
     * Sets up the layout for the screen and handles navigation features
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        verifyUserisLoggedIn()

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)

        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
        nav_view.setupWithNavController(newsNavHostFragment.findNavController())


        val myToolbar: Toolbar = findViewById<View>(R.id.main_toolbar) as Toolbar
        setSupportActionBar(myToolbar)

        toggle = ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        toggle.isDrawerIndicatorEnabled = true
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    /**
     * Verifies the user is logged in when opening the app
     *
     */
    private fun verifyUserisLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    /**
     * Handles user interactions with the overflow menu on the toolbar
     *
     * @param item
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            R.id.night_mode_toggle -> {
                if (AppCompatDelegate.getDefaultNightMode().toString() == "-100") {
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)

                } else if (AppCompatDelegate.getDefaultNightMode() == MODE_NIGHT_NO) {
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)

                } else {
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
                }
            }
        }

        if(item.itemId == android.R.id.home){
            drawer_layout.openDrawer(Gravity.LEFT)
        }

        nav_view.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_sports_news -> {
                    Log.d("sportsfragment", "clicked")
                    //nav_view.setupWithNavController(newsNavHostFragment.findNavController())
                    Log.d("sportsfragment", "clicked, setup")

                }

                //R.id.newsSource2 -> Toast.makeText(applicationContext, "News Source 2", Toast.LENGTH_SHORT).show()
                //R.id.newsSource3 -> Toast.makeText(applicationContext, "News Source 3", Toast.LENGTH_SHORT).show()
            }
            drawer_layout.closeDrawer(Gravity.LEFT)
            true
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * Handles toggling night mode on and off
     *
     * @param menu
     * @return true/false
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)

        if (AppCompatDelegate.getDefaultNightMode().toString() == "-100" ||
            AppCompatDelegate.getDefaultNightMode().toString() == "1"){
            menu?.findItem(R.id.night_mode_toggle)?.isChecked = false
        } else if (AppCompatDelegate.getDefaultNightMode().toString() == "2"){
            menu?.findItem(R.id.night_mode_toggle)?.isChecked = true
        }
        return super.onCreateOptionsMenu(menu)
    }





}