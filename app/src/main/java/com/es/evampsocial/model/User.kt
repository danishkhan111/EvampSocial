package com.es.evampsocial.model

data class User(val fName:String,
                val lName:String,
                val profilePicturePath:String?,
                val registrationTokens:MutableList<String>){
    constructor():this("","",null, mutableListOf())
}