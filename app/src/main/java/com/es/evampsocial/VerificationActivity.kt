package com.es.evampsocial

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.es.evampsocial.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class VerificationActivity : AppCompatActivity() {
    private var etCode:EditText?=null
    private var btnCode: Button?=null
    private var mProgressBar: ProgressBar? = null
    internal var codeSent: String? = null
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)
        btnCode=findViewById<View>(R.id.btnCodeVerification)as Button
        etCode=findViewById<View>(R.id.etNo) as EditText
        mProgressBar=ProgressBar(this)
        mAuth= FirebaseAuth.getInstance()

        btnCode!!.setOnClickListener {sendVerificationCode()  }
    }
    private fun sendVerificationCode() {
        val phone = etCode!!.text.toString()
        if (TextUtils.isEmpty(phone)) {
            etCode!!.error = "Phone Number Is Required"
            etCode!!.requestFocus()
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
            Toast.makeText(this@VerificationActivity, "Send Verfication Code Sucessfully", Toast.LENGTH_LONG).show() }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(this@VerificationActivity, " Send Verfication Code Failed", Toast.LENGTH_LONG).show() }

        override fun onCodeSent(s: String?, forceResendingToken: PhoneAuthProvider.ForceResendingToken?) {
            super.onCodeSent(s, forceResendingToken)
            codeSent = s
        }
    }
}
