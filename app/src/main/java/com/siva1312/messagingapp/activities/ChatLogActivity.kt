package com.siva1312.messagingapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.siva1312.messagingapp.R
import com.siva1312.messagingapp.models.ChatMessage
import com.siva1312.messagingapp.models.User
import com.siva1312.messagingapp.views.ChatFromItem
import com.siva1312.messagingapp.views.ChatToItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {

    companion object {
        val TAG = "ChatLog"
    }

    val adapter = GroupAdapter<GroupieViewHolder>()
    var fromUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        recyclerChatLog.adapter = adapter

        fromUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = fromUser?.userName

        listenForMessages()

        btSend.setOnClickListener {
            Log.d(TAG, "Attempt to send message...")
            sendMessage()
        }
    }

    private fun listenForMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = fromUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                if (chatMessage != null) {
                    Log.d(TAG, chatMessage.text)
                    //every time we add to the adapter, groupie dependency refreshes the adapter
                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        val currentUser =
                            RecentMessagesActivity.currentUser
                        adapter.add(ChatToItem(chatMessage.text, currentUser!!))
                    } else {
                        adapter.add(ChatFromItem(chatMessage.text, fromUser!!))
                    }
                }

                recyclerChatLog.scrollToPosition(adapter.itemCount - 1) //view get scrolled to last message when opened.
            }

            override fun onCancelled(error: DatabaseError) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

        })
    }

    private fun sendMessage() {
        //send the message to firebase and store it.
        val text = etTextMessage.text.toString()
        val fromId = FirebaseAuth.getInstance().uid ?: return //to get signed in user's uid
        val toId = fromUser?.uid

//        val reference = FirebaseDatabase.getInstance().getReference("/messages").push()
        val reference =
            FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
        val toReference =
            FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        val chatMessage =
            ChatMessage(reference.key!!, text, fromId, toId!!, System.currentTimeMillis() / 1000)

        reference.setValue(chatMessage).addOnSuccessListener {
            Log.d(TAG, "Saved chat message: ${reference.key}")
            etTextMessage.text.clear() //clear the message from edit text after send is pressed.
            recyclerChatLog.scrollToPosition(adapter.itemCount-1) // when a new message is sent the chat log scrolls to the last message.
        }
        toReference.setValue(chatMessage)

        val recentMessageReference = FirebaseDatabase.getInstance().getReference("/recent-messages/${fromId}/${toId}")
        recentMessageReference.setValue(chatMessage)

        val recentMessageToReference = FirebaseDatabase.getInstance().getReference("/recent-messages/${toId}/${fromId}")
        recentMessageToReference.setValue(chatMessage)
    }
}





