package com.es.evampsocial.model

data class ChatChannel(val usersIds:MutableList<String>) {
    constructor():this(mutableListOf())
}