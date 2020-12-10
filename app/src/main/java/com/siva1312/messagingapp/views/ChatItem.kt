package com.siva1312.messagingapp.views

import com.siva1312.messagingapp.R
import com.siva1312.messagingapp.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

//to display the chats of the sender
class ChatFromItem(val text: String, val fromUser: User) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.txtFromRow.text = text

        //load profile pic in the chat
        Picasso.get().load(fromUser.profilePicUrl).error(R.drawable.ic_person)
            .into(viewHolder.itemView.imgFromPic)
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}

//to display your chat messages
class ChatToItem(val text: String, val currentUser: User) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.txtToRow.text = text

        //load profile pic in the chat
        Picasso.get().load(currentUser.profilePicUrl).error(R.drawable.ic_person)
            .into(viewHolder.itemView.imgToPic)
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}