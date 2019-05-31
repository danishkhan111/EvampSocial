package com.es.evampsocial.MainView.Home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.es.evampsocial.MainView.Home.Add_Post.Add_PostActivity
import com.es.evampsocial.R
import com.es.evampsocial.RecyclerView.UsersListAdapter
import com.es.evampsocial.Util.FireStoreUtil
import com.es.evampsocial.model.Posts
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.ArrayList


class HomeFragment : Fragment() {

    lateinit var mFirestore: FirebaseFirestore
    private var mAuth: FirebaseAuth? = null
    private var imageUser: CircleImageView? = null
    private var mDocRef: DocumentReference? = null
    lateinit var textViewHome: TextView
    private val TAG = "FireLog"

    private var mMainList: RecyclerView? = null
    private var usersList: ArrayList<Posts>? = null
    private var usersListAdapter: UsersListAdapter? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)


        usersList = ArrayList()
        usersListAdapter = UsersListAdapter(usersList!!)



        mMainList = view.findViewById(R.id.recyclerView)
        mMainList!!.setHasFixedSize(true)
        mMainList!!.layoutManager = LinearLayoutManager(context)
        mMainList!!.adapter = usersListAdapter

        imageUser = view.findViewById(R.id.ivHome) as CircleImageView
        textViewHome = view.findViewById(R.id.textViewHome) as TextView
        mFirestore = FirebaseFirestore.getInstance()




        mFirestore.collection("Posts").addSnapshotListener { documentSnapshots, e ->
            if (e != null) {
                Log.d(TAG, "Error :" + e.message)
            }
            for (doc in documentSnapshots!!.documentChanges) {
                if (doc.type == DocumentChange.Type.ADDED) {
                    val post = doc.document.toObject<Posts>(Posts::class.java)
                    usersList!!.add(post)
                    usersListAdapter!!.notifyDataSetChanged()

                }
            }
        }


        val currentUser = FirebaseAuth.getInstance().currentUser
        val UId = currentUser!!.uid

        mDocRef = mFirestore.collection("Users").document(UId)

        mDocRef!!.get().addOnSuccessListener { documentSnapshot ->
            try {
                if (documentSnapshot != null) {

                    var imageView = documentSnapshot.getString("profilePicturePath")
                    if (imageView == null) {

                        retrieveImage(imageView)
                    } else {
                        retrieveImage(imageView)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }.addOnFailureListener {
            val message = "Name Did Not Fetch"
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }


        textViewHome.setOnClickListener {
            val intent = Intent(context, Add_PostActivity::class.java)
            startActivity(intent)
        }


        return view
    }

    private fun retrieveImage(imageView: String?) {

        FirebaseStorage.getInstance().reference.child(imageView!!).downloadUrl.addOnSuccessListener(object : OnSuccessListener<Uri> {
            override fun onSuccess(url: Uri?) {
                try {
                    if (url != null) {
                        Glide.with(context!!).load(url).into(ivHome)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()

                }

            }

        })
    }

}