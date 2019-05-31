package com.es.evampsocial.MainView.Friends

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.es.evampsocial.MainView.Friends.FriendsRequestsfregments.FriendRequests
import com.es.evampsocial.MainView.Friends.FriendsRequestsfregments.Friends
import com.es.evampsocial.MainView.Friends.FriendsRequestsfregments.SentRequests
import com.es.evampsocial.MainView.MainActivity
import com.es.evampsocial.R
import com.es.evampsocial.adapter.CustomPagerAdapter
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_friend_list.*

class FriendList: AppCompatActivity() {

    private var pagrAdapter: CustomPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_list)

        setSupportActionBar(toolbar)
        goback.setOnClickListener {
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        pagrAdapter= CustomPagerAdapter(supportFragmentManager)

        pagrAdapter!!.addFragments(Friends(),"Friends")
        pagrAdapter!!.addFragments(FriendRequests(),"Friend Requests")
        pagrAdapter!!.addFragments(SentRequests(),"Send Requests")
        customViewPager.adapter = pagrAdapter
    }

}
