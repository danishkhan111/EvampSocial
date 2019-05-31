package com.es.evampsocial.RecyclerView


import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.es.evampsocial.model.Posts
import com.es.evampsocial.R
import com.es.evampsocial.Util.StorageUtil
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.post_list.*
import com.xwray.groupie.kotlinandroidextensions.ViewHolder


 data class PostItem(val person: Posts,
               val userId: String,
               private val context: Context)
    : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {

//        viewHolder.textViewName.text= person.fName
//        viewHolder.textViewPost.text = person.textPost
//        if (person.profilePicturePath != null)
//            Glide.with(context)
//                    .load(StorageUtil.pathToReference(person.profilePicturePath))
//                    .apply(RequestOptions().placeholder( R.drawable.ic_account_circle_black_24dp))
//                    .into(viewHolder.userImagePost)
    }

    override fun getLayout() = R.layout.post_list
}