package com.es.evampsocial.MainView.Profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.es.evampsocial.R
import com.es.evampsocial.Util.FireStoreUtil
import com.es.evampsocial.Util.StorageUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.io.ByteArrayOutputStream
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import android.net.Uri
import android.text.TextUtils
import android.widget.*
import de.hdodenhof.circleimageview.CircleImageView


class ProfileFragment : Fragment() {
    private var editTextName: AutoCompleteTextView? = null
    private var mAuth: FirebaseAuth? = null
    private var mFireStore: FirebaseFirestore? = null
    private var mDocRef: DocumentReference? = null
    private var btnUpdateProfile: Button? = null
    private val RC_SELECT_IMAGE = 2
    private var pictureJustChanged = false
    private var editText_fName:AutoCompleteTextView?=null
    private var editText_lName:AutoCompleteTextView?=null
    private var mProgressBar:ProgressBar?=null
    private var imageView:ImageView?=null
    private lateinit var selectedImageBytes: ByteArray
    private var mStorage:FirebaseStorage?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        editText_fName=view.findViewById(R.id.editText_fName) as AutoCompleteTextView
        editText_lName=view.findViewById(R.id.editText_lName) as AutoCompleteTextView
        btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile) as Button
        mProgressBar=view.findViewById(R.id.new_profile_progressBar)
        imageView=view.findViewById(R.id.profile_image)
        mStorage= FirebaseStorage.getInstance()

        mProgressBar!!.visibility=View.VISIBLE
        view.apply {
            profile_image.setOnClickListener {
                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                }
                startActivityForResult(Intent.createChooser(intent, "Select Image"), RC_SELECT_IMAGE)
            }
        }




        btnUpdateProfile!!.setOnClickListener {

            if (::selectedImageBytes.isInitialized) {
                StorageUtil.uploadProfilePhoto(selectedImageBytes) { imagePath ->
                    FireStoreUtil.updateCurrentUser(editText_fName!!.text.toString(),
                            editText_lName!!.text.toString(), imagePath)
                    Toast.makeText(context, "Profile Photo Uploaded Successfully", Toast.LENGTH_LONG).show()
                }
            } else {
                FireStoreUtil.updateCurrentUser(editText_fName!!.text.toString(),
                        editText_lName!!.text.toString(), null)
                Toast.makeText(context, "Data Saved Successfully", Toast.LENGTH_LONG).show()
            }
        }
        mFireStore = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val UId = currentUser!!.uid
        mAuth = FirebaseAuth.getInstance()
        mDocRef = mFireStore!!.collection("Users").document(UId)

        mDocRef!!.get().addOnSuccessListener { documentSnapshot ->
            try {
                if (documentSnapshot != null) {


                    var fname = documentSnapshot.getString("fName")
                    var lname = documentSnapshot.getString("lName")
                    var imageView = documentSnapshot.getString("profilePicturePath")
                    if (TextUtils.isEmpty(fname) && TextUtils.isEmpty(lname) && imageView == null) {
                        editText_fName!!.setText(fname)
                        editText_lName!!.setText(lname)


                        retrieveImage(imageView)
                    }else{
                        editText_fName!!.setText(fname)
                        editText_lName!!.setText(lname)

                        retrieveImage(imageView)
                    }
                }
            }catch (e:Exception){
                e.printStackTrace()
            }

        }.addOnFailureListener {
            val message = "Name Did Not Fetch"
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        return view
    }

    private fun retrieveImage(imageView: String?) {

        FirebaseStorage.getInstance().reference.child(imageView!!).downloadUrl.addOnSuccessListener(object : OnSuccessListener<Uri> {
            override fun onSuccess(url: Uri?) {
                try {
                    if (url !=null) {
                        Glide.with(context!!).load(url).into(profile_image)
                        mProgressBar!!.visibility = View.INVISIBLE
//                var a=0
                    }
                }catch (e:Exception){
                    e.printStackTrace()

                }

            }

        });
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImagePath = data.data
            var selectedImageBmp = MediaStore.Images.Media
                    .getBitmap(activity?.contentResolver, selectedImagePath)
            val outputStream = ByteArrayOutputStream()
            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            selectedImageBytes = outputStream.toByteArray()

            Glide.with(this)
                    .load(selectedImageBytes)
                    .into(profile_image)
            pictureJustChanged = true
//        profile_image.setImageBitmap(selectedImageBmp)

        }
    }


}


