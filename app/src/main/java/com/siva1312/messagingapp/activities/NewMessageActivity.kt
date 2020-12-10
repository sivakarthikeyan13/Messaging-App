package com.siva1312.messagingapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.siva1312.messagingapp.R
import com.siva1312.messagingapp.models.User
import com.siva1312.messagingapp.views.NewMessageRowItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        supportActionBar?.title = "Select User"

        getUsers()
    }

    companion object{
        val USER_KEY = "USER_KEY"
    }

    private fun getUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        //the user details are call only once from the database(only single event)
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            //this function is used to get all data from the database
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()
                snapshot.children.forEach{
                    val user = it.getValue(User::class.java)
                    if (user != null){
                        Log.d("NewMessageActivity", "got user details")
                        adapter.add(NewMessageRowItem(user))
                    }
                }

                adapter.setOnItemClickListener { item, view ->

                    val userItem = item as NewMessageRowItem
                    val intent = Intent(view.context, ChatLogActivity::class.java)
                    intent.putExtra(USER_KEY,userItem.user)
                    startActivity(intent)
                    finish()
                }
                recyclerNewMessages.adapter = adapter
                recyclerNewMessages.addItemDecoration(DividerItemDecoration(this@NewMessageActivity, DividerItemDecoration.VERTICAL)) //display a horizontal line between two items
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("NewMessageActivity", "failed to get user details", error.toException())
            }
        })
    }
}

