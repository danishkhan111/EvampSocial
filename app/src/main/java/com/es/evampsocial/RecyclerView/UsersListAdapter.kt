package com.es.evampsocial.RecyclerView

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.es.evampsocial.R
import com.es.evampsocial.Util.StorageUtil
import com.es.evampsocial.model.Posts
import de.hdodenhof.circleimageview.CircleImageView


class UsersListAdapter(var usersList: List<Posts>) : RecyclerView.Adapter<UsersListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvname.text = usersList[position].textPost

//        holder.tvDept.text = usersList[position].department
//        if (Posts.pro != null)
//            Glide.with(this)
//                    .load(StorageUtil.pathToReference(path))
//                    .apply(RequestOptions().placeholder( R.drawable.ic_account_circle_black_24dp))
//                    .into(holder.PostByImage)
    }

    override fun getItemCount(): Int {

        return usersList.size
    }

    inner class ViewHolder(internal var mView: View) : RecyclerView.ViewHolder(mView) {

        var tvname: TextView
//        var PostByImage: CircleImageView
//        var tvDept: TextView

        init {

            tvname = mView.findViewById<View>(R.id.textViewName) as TextView
//            PostByImage = mView.findViewById<View>(R.id.userImagePost) as CircleImageView
//            tvDept = mView.findViewById<View>(R.id.tvDepartment) as TextView

        }
    }

}