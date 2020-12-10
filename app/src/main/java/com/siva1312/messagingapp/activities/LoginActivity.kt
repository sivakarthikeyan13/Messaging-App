package com.siva1312.messagingapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.siva1312.messagingapp.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    companion object {
        const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btLogin.setOnClickListener {
            signIn()
        }


        txtRegister.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }


    }

    private fun signIn() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        Log.d(TAG, "email is$email")
        Log.d(TAG, "password is$password")
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                this, "Enter all the details",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(TAG, "loginUserWithEmail:success")
                    val intent = Intent(this, RecentMessagesActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                } else return@addOnCompleteListener
            }
            .addOnFailureListener {
                Log.d(TAG, "Login Failed")
                Toast.makeText(
                    this, "Failed to Login: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }
}