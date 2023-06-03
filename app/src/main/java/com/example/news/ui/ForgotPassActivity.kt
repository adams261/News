package com.example.news.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.news.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_forgot_password.*

/**
 * Handles the user forgetting their password
 *
 */
class ForgotPassActivity : AppCompatActivity() {

    /**
     * Sets up the layout and handles entries
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        forgotPass_forgotPassButton.setOnClickListener{
            val email = email_forgotPass.text.toString()

            Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("ForgotPass", "Password reset email sent.")
                        Toast.makeText(applicationContext,"Password reset email sent",Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
        }

        back_to_login.setOnClickListener {
            finish()
        }
    }

    /**
     * hides the keyboard
     *
     * @param view
     */
    fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Hides the keyboard when the user taps elsewhere on the screen
     *
     * @param hasFocus
     */
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        email_forgotPass.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        }
        super.onWindowFocusChanged(hasFocus)
    }

}