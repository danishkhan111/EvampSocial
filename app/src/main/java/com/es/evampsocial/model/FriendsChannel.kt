package com.es.evampsocial.model

data class FriendsChannel(val userIds:MutableList<String>) {
    constructor():this(mutableListOf())
}