package com.es.evampsocial.model

import java.util.*

object MessageType{
    const val TEXT="TEXT"
    const val IMAGE="IMAGE"
}

interface Message {
    val time:Date
    val senderId:String
    val recepientID:String
    val SenderName:String
    val type:String
}