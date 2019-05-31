package com.es.evampsocial.RecyclerView

import android.content.Context
import com.bumptech.glide.Glide
import com.es.evampsocial.R
import com.es.evampsocial.Util.StorageUtil
import com.es.evampsocial.model.*
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_person.*


class PersonItem(val person: User,
                 val userId:String,
                 private val context: Context)
        : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {


        viewHolder.textView_name.text = person.fName

        /* viewHolder.select_person.visibility=View.INVISIBLE*/

        try {
            if (person.profilePicturePath != null)
                Glide.with(context)
                        .load(StorageUtil.pathToReference(person.profilePicturePath))
                        .placeholder(R.drawable.ic_account_circle_black_24dp)
                        .dontTransform()
                        .dontAnimate()
                        .into(viewHolder.imageView_profile_picture)


        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun getLayout()= R.layout.item_person


}