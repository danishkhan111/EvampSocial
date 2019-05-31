package com.es.evampsocial.MainView

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabItem
import android.support.design.widget.TabLayout
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.es.evampsocial.Login.LoginActivity
import com.es.evampsocial.MainView.AllUsers.AllUsersActivity
import com.es.evampsocial.MainView.Friends.FriendList
import com.es.evampsocial.MainView.Home.HomeFragment
import com.es.evampsocial.MainView.Profile.ProfileFragment
import com.es.evampsocial.MainView.Status.StatusFragment
import com.es.evampsocial.MainView.chat.ChatFragment
import com.es.evampsocial.R
import com.es.evampsocial.Search.SearchUser
import com.es.evampsocial.adapter.CustomPagerAdapter
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header_main.*


class MainActivity : AppCompatActivity() {
    private var toolBar: Toolbar? = null
    private var tablayOut: TabLayout? = null
    private var pagrAdapter: CustomPagerAdapter? = null
    private var tabChats: TabItem? = null
    private var tabStatus: TabItem? = null
    private var tabProfile: TabItem? = null
    private var drawer:DrawerLayout?=null


    private var mAuth: FirebaseAuth? = null
    private var mFireStore: FirebaseFirestore? = null
    private var mDocRef: DocumentReference? = null
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolBar = findViewById<View>(R.id.toolbar) as Toolbar
        drawer=findViewById<View>(R.id.drawer_layout) as DrawerLayout
        setSupportActionBar(toolBar)
        pagrAdapter = CustomPagerAdapter(supportFragmentManager)

        pagrAdapter!!.addFragments(HomeFragment(), "HOME")
        pagrAdapter!!.addFragments(ChatFragment(),"CHAT")
        //pagrAdapter!!.addFragments(StatusFragment(), "STATUS")
        pagrAdapter!!.addFragments(ProfileFragment(), "PROFILE")
        customViewPager.adapter = pagrAdapter
        customTabLayout.setupWithViewPager(customViewPager)



        val navigationView = findViewById<View>(R.id.nav_View) as NavigationView
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id. nav_All_Users-> {
                val intent=Intent(this@MainActivity,AllUsersActivity::class.java)
                    startActivity(intent)
                    drawer!!.closeDrawers()

                }
                R.id.nav_Friends -> {
                    val intent=Intent(this@MainActivity,FriendList::class.java)
                    startActivity(intent)
                    drawer!!.closeDrawers()
                }
                R.id.nav_Add_Posts -> {
//                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ResturantsFrangment()).addToBackStack(null).commit()
                    drawer!!.closeDrawers()
                }
                R.id.nav_search_users ->{
                    val intent=Intent(this@MainActivity,SearchUser::class.java)
                    startActivity(intent)
                    drawer!!.closeDrawers()

                }
                R.id.nav_share -> {
//                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Shopping_Fragment()).addToBackStack(null).commit()
                    drawer!!.closeDrawers()
                }
                R.id.nav_logout -> {
                    FirebaseAuth.getInstance().signOut()

                    drawer!!.closeDrawers()
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                }
            }
            true
        }
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()







        mFireStore = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val UId = currentUser!!.uid
        mAuth = FirebaseAuth.getInstance()
        mDocRef = mFireStore!!.collection("Users").document(UId)

        mDocRef!!.get().addOnSuccessListener { documentSnapshot ->
            try {
                if (documentSnapshot != null) {


                    var fname = documentSnapshot.getString("fName")
                    var email = documentSnapshot.getString("Email")
                    var imageView = documentSnapshot.getString("profilePicturePath")
                    if (TextUtils.isEmpty(fname) && TextUtils.isEmpty(email) && imageView ==null) {
                        tvHeaderName!!.setText(fname)
                        tvHeaderEmail!!.setText(email)
                        retrieveImage(imageView)
                    }else{
                        tvHeaderName!!.setText(fname)
                        tvHeaderEmail!!.setText(email)
                        retrieveImage(imageView)
                    }
                }
            }catch (e:Exception){
                e.printStackTrace()
            }

        }.addOnFailureListener {
            val message = "Name Did Not Fetch"
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }


    }

    private fun retrieveImage(imageView: String?) {

        FirebaseStorage.getInstance().reference.child(imageView!!).downloadUrl.addOnSuccessListener(object : OnSuccessListener<Uri> {
            override fun onSuccess(url: Uri?) {
                try {
                    if (url !=null) {
                        Glide.with(applicationContext).load(url).into(profile_image_header)

//                var a=0
                    }
                }catch (e:Exception){
                    e.printStackTrace()

                }

            }

        });
    }


}


