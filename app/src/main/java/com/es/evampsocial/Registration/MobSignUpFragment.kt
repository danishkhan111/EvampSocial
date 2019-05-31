package com.es.evampsocial.Registration


import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.es.evampsocial.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_mob_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up_email.*
import java.util.concurrent.TimeUnit


class MobSignUpFragment : Fragment() {
    private var mAuth: FirebaseAuth? = null
    private var mfireStore: FirebaseFirestore?=null


    internal var codeSent: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_mob_sign_up, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnEmailReg.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.signUpEmailFragment))
        mAuth=FirebaseAuth.getInstance()
        mfireStore= FirebaseFirestore.getInstance()
        var fName=arguments?.getString("fnameArg")
       var lName=arguments?.getString("lastNameArg")
       btnGetMobileCode.setOnClickListener{
//           getVerificationCode()
       }
    }
//    private fun getVerificationCode(){
//
//
//            val phone = atvMobReg!!.text.toString()
//            if (TextUtils.isEmpty(phone)) {
//                atvMobReg.setError("Please Enter Number")
//                return
//            }
//
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                phone = phone,
//                i = 60,
//                seconds = TimeUnit.SECONDS,
//                mobSignUpFragment = this,
//                mCallBacks = mCallBacks)
//        }
//        internal var mCallBacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
//                Toast.makeText(context, "Send Verfication Code Sucessfully", Toast.LENGTH_LONG).show() }
//
//            override fun onVerificationFailed(e: FirebaseException) {
//                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
//            }
//            override fun onCodeSent(s: String?, forceResendingToken: PhoneAuthProvider.ForceResendingToken?) {
//                super.onCodeSent(s, forceResendingToken)
//                codeSent = s
//            }
//        }
    }





