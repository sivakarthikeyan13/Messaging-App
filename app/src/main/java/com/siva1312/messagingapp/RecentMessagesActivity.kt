package com.siva1312.messagingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class RecentMessagesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recent_messages)

        userSignedInVerification()
    }

    private fun userSignedInVerification(){
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null){
            val intent = Intent(this, LoginActivity::class.java)
            //clears all activity in task so on clicking back button exits app
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    //to perform action on selecting each menu item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.action_new_message -> {
                val intent = Intent(this, NewMessageActivity::class.java)
                startActivity(intent)
            }
            R.id.action_logout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                //clears all activity in task so on clicking back button exits app
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //to display the menu items in toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_dashboard, menu)
        return super.onCreateOptionsMenu(menu)
    }
}