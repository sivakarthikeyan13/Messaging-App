package com.siva1312.messagingapp.views

import com.siva1312.messagingapp.R
import com.siva1312.messagingapp.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.recycler_new_message_row.view.*

class NewMessageRowItem(val user: User): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        //assign values to each recycler row
        viewHolder.itemView.txtUserName.text = user.userName
        Picasso.get().load(user.profilePicUrl).error(R.drawable.ic_person).into(viewHolder.itemView.imgUserPic)
    }
    override fun getLayout(): Int {
        //gets the layout of the recycler view
        return R.layout.recycler_new_message_row
    }
}