package com.es.evampsocial.model

data class Posts (val textPost: String,
                  val Name_By_Posted:String,
                  val Image_By_Posted:String?)
{
constructor():this("","",null){}

}

