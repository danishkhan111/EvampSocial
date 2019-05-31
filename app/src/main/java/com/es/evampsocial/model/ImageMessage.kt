package com.es.evampsocial.model

import java.util.*

data class ImageMessage(val ImagePath:String,
                        override val time: Date,
                        override val senderId:String,
                        override val recepientID: String,
                        override val SenderName: String,
                        override val type:String= MessageType.IMAGE)
    :Message{
    constructor():this("", Date(0),"","","")
}