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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*

/**
 * Handles the user registering to use the app
 *
 */
class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    /**
     * Sets up the layout and handles entries
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        registerButton.setOnClickListener {
            val email = email_register.text.toString()
            val password = password_register.text.toString()

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                   if (!it.isSuccessful) return@addOnCompleteListener
                    saveUserInterestsToDatabase()
                }
                .addOnFailureListener{
                    Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

        loginButton_register.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Saves the user interests to a Firebase Database
     *
     */
    private fun saveUserInterestsToDatabase(){
            val uid = FirebaseAuth.getInstance().uid
            val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
            val name = name_register.text.toString()
            val interests = interests_register.text.toString()
            val user = User(uid.toString(), name, interests)
            ref.setValue(user).addOnSuccessListener {
                val intent = Intent(this, NewsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
    }

    /**
     * hides the keyboard
     *
     * @param view
     */
    private fun hideKeyboard(view: View) {
        val inputMethodManager: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Hides the keyboard when the user taps elsewhere on the screen
     *
     * @param hasFocus
     */
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        name_register.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        }
        password_register.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        }
        email_register.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        }
        interests_register.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        }
        super.onWindowFocusChanged(hasFocus)
    }
}

