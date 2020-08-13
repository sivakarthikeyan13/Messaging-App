package com.siva1312.messagingapp

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

class LoginActivity : AppCompatActivity() {

    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var login: Button
    lateinit var forgotPassword: TextView
    lateinit var register: TextView

    private val TAG = "LoginActivity"
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.etEmail)
        password = findViewById(R.id.etPassword)
        login = findViewById(R.id.btLogin)
        forgotPassword = findViewById(R.id.txtForgotPassword)
        register = findViewById(R.id.txtRegister)

        auth = Firebase.auth

        Log.d(TAG, "email is$email")
        Log.d(TAG, "password is$password")

        login.setOnClickListener {
            signIn()
        }

        forgotPassword.setOnClickListener { }

        register.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }


    }

    private fun signIn(){
        var lEmail = email.text.toString()
        var lPassword = password.text.toString()
        if (lEmail.isEmpty() || lPassword.isEmpty()){
            Toast.makeText(this, "Enter all the details",
                Toast.LENGTH_SHORT).show()
            return
        }
        auth.signInWithEmailAndPassword(lEmail, lPassword)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    Log.d(TAG, "loginUserWithEmail:success")
                }else return@addOnCompleteListener
            }
            .addOnFailureListener{
                Log.d(TAG, "Login Failed")
                Toast.makeText(this, "Failed to Login: ${it.message}",
                    Toast.LENGTH_SHORT).show()
            }

    }
}