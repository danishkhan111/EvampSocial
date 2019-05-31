package com.es.evampsocial.Registration


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.es.evampsocial.MainView.MainActivity
import com.es.evampsocial.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_fragment_login.*
import kotlinx.android.synthetic.main.fragment_sign_up_email.*


class SignUpEmailFragment : Fragment() {
    var mAuth: FirebaseAuth? = null
    private var email: String? = null
    private var password: String? = null
    private var fName: String? = null
    private var lName: String? = null
    private var mFireStore: FirebaseFirestore? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        mFireStore = FirebaseFirestore.getInstance()
        fName = arguments?.getString("fnameArg")
        lName = arguments?.getString("lastNameArg")
        btnVerifyStatus.setOnClickListener(View.OnClickListener {
            FirebaseAuth.getInstance().currentUser!!.reload().addOnCompleteListener { verifyEmail() }
        })
        btnMobSignUp.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.mobSignUpFragment))




        btnSignUp.setOnClickListener { SignInWithEmailPass() }
    }

    private fun SignInWithEmailPass() {
        email = atvEmailSignUp.text.toString()
        password = atvPasswordSignUp.text.toString()

        if (TextUtils.isEmpty(email)) {
            atvEmailSignUp.setError("Please Enter Email")
            return
        }
        if (TextUtils.isEmpty(password)) {
            atvPasswordSignUp.setError("Please Enter Password")
            return
        } else {
            mAuth!!.createUserWithEmailAndPassword(email!!, password!!)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            sendEmailVerification()
                            atvEmailSignUp.setText("")
                            atvPasswordSignUp.setText("")
                            val currentUser = FirebaseAuth.getInstance().currentUser
                            val UId = currentUser!!.uid
                            val userMap = HashMap<String, String>()
                            userMap.put("fName", fName!!)
                            userMap.put("lName", lName!!)
                            userMap.put("Mobile No", "")
                            userMap.put("FCM ID", "")
                            userMap.put("Email", email!!)
                            userMap.put("profilePicturePath", "")
                            mFireStore!!.collection("Posts").document(UId).set(userMap).addOnSuccessListener {

                            }.addOnFailureListener {
                                Toast.makeText(context, "Name Submit Failed", Toast.LENGTH_LONG).show()
                            }
                            Toast.makeText(context, "Your Account Created Successfully", Toast.LENGTH_LONG).show()
                            val intent = Intent(context, MainActivity::class.java)
                            startActivity(intent)


                        } else if (!task.isSuccessful) {
                            Toast.makeText(context, task.exception?.message, Toast.LENGTH_LONG).show()
                        }

                    }
        }


    }

    private fun sendEmailVerification() {
        val mUser = mAuth!!.currentUser
        mUser!!.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                verifyEmail()
            } else {
                Toast.makeText(context, "Email is Not sent To your Account", Toast.LENGTH_LONG).show()
            }
        }


    }

    private fun verifyEmail() {
        val mUser = mAuth!!.currentUser
        if (mUser!!.isEmailVerified) {
            btnVerifyStatus.setImageResource(R.drawable.tick)
            Toast.makeText(context, "Email Verified Successfully", Toast.LENGTH_LONG).show()

            return
        } else {

            btnVerifyStatus.setImageResource(R.drawable.cross)
            Toast.makeText(context, "First Verify Your Email", Toast.LENGTH_LONG).show()
            return
        }


    }


}
