package com.es.evampsocial.RecyclerView


import android.content.Context
import com.es.evampsocial.R
import com.es.evampsocial.model.User
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.sentrequestitemfriend.*


class ReceivedrequestfriendItem(val person:User,
                                val userId:String,
                                private val context: Context): Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView_name.text=person.fName

        if (person.profilePicturePath!=null){
            /*Glide.with(context).load(StorageUtil.pathToReference(person.profilePicturePath))

                    .apply(requestOptions)
                    .load(R.drawable.ic_account_circle_black_24dp)
                    .into(viewHolder.imageView_profile_picture)*/
        }
    }

    override fun getLayout()= R.layout.receivedfriendrequestitem
}