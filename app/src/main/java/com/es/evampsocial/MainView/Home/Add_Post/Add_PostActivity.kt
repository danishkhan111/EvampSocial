package com.es.evampsocial.MainView.Home.Add_Post

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import com.bumptech.glide.Glide
import com.es.evampsocial.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_add__post.*
import kotlinx.android.synthetic.main.fragment_home.*

class Add_PostActivity : AppCompatActivity() {
    lateinit var mFirestore: FirebaseFirestore
    private var mAuth: FirebaseAuth?=null
    private var imageUser: CircleImageView?=null
    private var mDocRef: DocumentReference? = null
    lateinit var UId: String
    private var fname:String?=null
    private var imageView:String?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add__post)
        mFirestore= FirebaseFirestore.getInstance()

        mAuth=FirebaseAuth.getInstance()
        imageUser=findViewById(R.id.ivPost) as CircleImageView

        addPostShare.setOnClickListener {
            Addpost()
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        UId = currentUser!!.uid



        mDocRef = mFirestore.collection("Users").document(UId)

        mDocRef!!.get().addOnSuccessListener { documentSnapshot ->
            try {
                if (documentSnapshot != null) {
                     fname = documentSnapshot.getString("fName")

                    imageView = documentSnapshot.getString("profilePicturePath")
                    if ( TextUtils.isEmpty(fname) && imageView == null) {
                        tvAddPostUserName.setText(fname)
                        retrieveImage(imageView)
                    }else{
                        tvAddPostUserName.setText(fname)
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
                        Glide.with(applicationContext).load(url).into(ivPost)

//                var a=0
                    }
                }catch (e:Exception){
                    e.printStackTrace()

                }

            }

        });
    }
    private fun Addpost(){

        mDocRef = mFirestore.collection("Users").document(UId)

        mDocRef!!.get().addOnSuccessListener { documentSnapshot ->
            try {
                if (documentSnapshot != null) {
                   fname = documentSnapshot.getString("fName")

                    imageView = documentSnapshot.getString("profilePicturePath")
                    val userMap=HashMap<String, String>()
                    var post=editTextPost.text.toString()
                    var name=fname.toString()
                    var posted_By_Image=imageView.toString()

                    userMap.put("textPost", post)
                    userMap.put("Name_By_Posted",name)
                    userMap.put("Image_By_Posted",posted_By_Image)
                    mFirestore.collection("Posts").add(userMap).addOnSuccessListener {
                        Toast.makeText(this,"Post Submit Successfully",Toast.LENGTH_LONG).show()
                    }.addOnFailureListener{
                        Toast.makeText(this,"Not Saved",Toast.LENGTH_LONG).show()
                    }
                }
            }catch (e:Exception){
                e.printStackTrace()
            }

        }.addOnFailureListener {
            val message = "Name Did Not Fetch"
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }


//        val userMap=HashMap<String, String>()
//        var post=editTextPost.text.toString()
//
//
//        userMap.put("textPost", post)
//        userMap.put("Posted by Name", name.toString())
//
//        mFirestore.collection("Posts").add(userMap).addOnSuccessListener {
//            editTextPost.setText("")
//        }.addOnFailureListener{
//            Toast.makeText(applicationContext,"PostShared",Toast.LENGTH_LONG).show()
//        }
    }

}
