package com.siva1312.messagingapp.views

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.siva1312.messagingapp.R
import com.siva1312.messagingapp.models.ChatMessage
import com.siva1312.messagingapp.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.recycler_recent_messages_row.view.*

class RecentMessagesRowItem(val recentMessage: ChatMessage): Item<GroupieViewHolder>() {
    var chatPartnerUser: User? = null

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.txtRecentMessage.text = recentMessage.text

        val chatPartnerId: String = if (recentMessage.fromId == FirebaseAuth.getInstance().uid){
            recentMessage.toId
        }else{
            recentMessage.fromId
        }
        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatPartnerUser = snapshot.getValue(User::class.java)
                viewHolder.itemView.txtRecentUserName.text = chatPartnerUser?.userName

                Picasso.get().load(chatPartnerUser?.profilePicUrl).error(R.drawable.ic_person).into(viewHolder.itemView.imgRecentProfilePic)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun getLayout(): Int {
        return R.layout.recycler_recent_messages_row
    }

}