package com.siva1312.messagingapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.siva1312.messagingapp.R
import com.siva1312.messagingapp.models.ChatMessage
import com.siva1312.messagingapp.models.User
import com.siva1312.messagingapp.views.RecentMessagesRowItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_recent_messages.*

class RecentMessagesActivity : AppCompatActivity() {

    companion object{
        var currentUser: User? = null
    }

    val recentMessagesMap = HashMap<String, ChatMessage>()  //hash map to store id and recent messages of users
    val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recent_messages)

        recyclerRecentMessages.adapter = adapter
        recyclerRecentMessages.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL)) //display a horizontal line between two items

        adapter.setOnItemClickListener { item, view ->
            val intent = Intent(this, ChatLogActivity::class.java)
            val row = item as RecentMessagesRowItem //casting item as RecentMessagesRow
            intent.putExtra(NewMessageActivity.USER_KEY, row.chatPartnerUser)
            startActivity(intent)
        }

        getCurrentUser()
        userSignedInVerification()
        listenForRecentMessages()
    }

    private fun refreshRecentMessagesRecyclerView(){
        adapter.clear() //clear all  rows in recycler view
        recentMessagesMap.values.forEach {
            adapter.add(RecentMessagesRowItem(it))
        }
    }

    private fun listenForRecentMessages(){
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/recent-messages/$fromId")
        ref.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?:return
                recentMessagesMap[snapshot.key!!] = chatMessage
                refreshRecentMessagesRecyclerView()
            }

            //when new message is received, this is called to refresh recent_messages_recycler view
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?:return
                recentMessagesMap[snapshot.key!!] = chatMessage
                refreshRecentMessagesRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

        })
    }

    private fun getCurrentUser(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
                Log.d("RecentMessages", "current user: ${currentUser?.userName}")
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
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