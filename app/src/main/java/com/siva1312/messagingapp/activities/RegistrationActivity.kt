package com.siva1312.messagingapp.activities

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.siva1312.messagingapp.R
import com.siva1312.messagingapp.models.User
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.*

class RegistrationActivity : AppCompatActivity() {

    lateinit var name: String
    companion object{
        const val TAG = "RegistrationActivity"
    }


    var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        btnProfilePic.setOnClickListener {
            Log.d(TAG, "photo selector")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        btRegister.setOnClickListener {
            signUp()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d(TAG, "Photo is selected")

            photoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, photoUri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            imgProfilePic.setImageDrawable(bitmapDrawable)
            btnProfilePic.alpha = 0f
        }
    }

    private fun signUp() {
         name = etName.text.toString()
        val email = etEmail.text.toString()
        val password = etRegisterPassword.text.toString()
        val confirmPassword = etRegisterConfirmPassword.text.toString()

        Log.d(TAG, "email is$email")
        Log.d(TAG, "password is$password")

        if (name.isEmpty() || email.isEmpty() || email.isEmpty()) {
            Toast.makeText(
                this, "Enter all the details",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (password != confirmPassword) {
            Toast.makeText(
                this, "Password does not match",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(
                        this, "Registered.",
                        Toast.LENGTH_SHORT
                    ).show()

                    uploadProfilePic()
                } else return@addOnCompleteListener
            }
            .addOnFailureListener {
                Log.d(TAG, "Registration Failed")
                Toast.makeText(
                    this, "Failed to Register: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun uploadProfilePic() {
        if (photoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(photoUri!!)
            .addOnSuccessListener {
                Log.d(TAG, "image added successfully: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d(TAG, "got image location")
                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "failed to add image: ${it.message}")
            }
    }

    private fun saveUserToFirebaseDatabase(profilePicUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, name, profilePicUrl)

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d(TAG, "user saved to firebase Database")

                val intent = Intent(this, RecentMessagesActivity::class.java)
                //clears all activity in task so on clicking back button exits app
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.d(TAG, "failed to save user to firebase Database: ${it.message}")
            }
    }

}

