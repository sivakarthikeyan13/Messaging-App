package com.siva1312.messagingapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid: String, val userName: String, val profilePicUrl: String): Parcelable{
    //no argument constructor
    constructor() : this("error","error", "error")
}

