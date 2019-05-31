package com.es.evampsocial.RecyclerView

import android.content.Context
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.es.evampsocial.R
import com.es.evampsocial.Util.StorageUtil
import com.es.evampsocial.model.ImageMessage
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.iem_image_message.*
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions




class ImageMessageItem(val message:ImageMessage,
                       val context: Context)
    :MessageItem(message){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        super.bind(viewHolder, position)

        /*  val options = RequestOptions()
                .placeholder(R.drawable.ic_image_black_24dp)
                .error(R.drawable.ic_account_circle_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                *//*.priority(Priority.HIGH)*//*
                .dontAnimate()
                .dontTransform()*/
        try {


            Glide.with(View(context))
                    .load(StorageUtil.pathToReference(message.ImagePath))
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .dontAnimate()
                    .dontTransform()
                    .into(viewHolder.imageView_message_image)
        }catch (e:Exception)
        {
            e.printStackTrace()
        }
    }

    override fun getLayout()= R.layout.iem_image_message
    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is ImageMessageItem)
            return false
        if (this.message!=other.message)
            return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? ImageMessageItem)
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }

}