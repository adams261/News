package com.example.news.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.news.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Handles the user logging in to their account
 *
 */

class LoginActivity : AppCompatActivity() {

    /**
     * Sets up the layout and handles entries
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        loginButton_login.setOnClickListener {
            val email = email_login.text.toString()
            val password = password_login.text.toString()

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener{
                if(it.isSuccessful) {
                    val intent = Intent(this, NewsActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(applicationContext,"Error: Incorrect email/password entered", Toast.LENGTH_SHORT).show()
                }
            }
        }

        forgotPass.setOnClickListener{
            Log.d("Login", "Show forgot password activity")
            val intent = Intent(this, ForgotPassActivity::class.java)
            startActivity(intent)
        }


        back_to_registration.setOnClickListener {
            finish()
        }
    }

    /**
     * hides the keyboard
     *
     * @param view
     */
    fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Hides the keyboard when the user taps elsewhere on the screen
     *
     * @param hasFocus
     */
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        email_login.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        }
        password_login.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        }
        super.onWindowFocusChanged(hasFocus)
    }


}