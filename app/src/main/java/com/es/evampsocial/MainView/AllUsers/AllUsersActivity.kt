package com.es.evampsocial.MainView.AllUsers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.es.evampsocial.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_all_users.*
import kotlinx.android.synthetic.main.fragment_mob_sign_up.*
import java.util.concurrent.TimeUnit

class AllUsersActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var mfireStore: FirebaseFirestore?=null


    internal var codeSent: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_users)
        mAuth=FirebaseAuth.getInstance()
        mfireStore= FirebaseFirestore.getInstance()
        btnGetMobileCodeActivity.setOnClickListener{
            getVerificationCode()
        }
    }
    private fun getVerificationCode(){


        val phone = atvMobRegActivity!!.text.toString()
        if (TextUtils.isEmpty(phone)) {
            atvMobRegActivity.setError("Please Enter Number")
            return
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                this,
                mCallBacks)
    }
    internal var mCallBacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
            Toast.makeText(this@AllUsersActivity, "Send Verfication Code Sucessfully", Toast.LENGTH_LONG).show() }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(this@AllUsersActivity, e.message, Toast.LENGTH_LONG).show()
        }
        override fun onCodeSent(s: String?, forceResendingToken: PhoneAuthProvider.ForceResendingToken?) {
            super.onCodeSent(s, forceResendingToken)
            codeSent = s
        }
    }
}
