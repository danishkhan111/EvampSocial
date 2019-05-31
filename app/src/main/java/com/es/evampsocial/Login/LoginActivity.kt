package com.es.evampsocial.Login


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import com.es.evampsocial.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private var etEmail: AutoCompleteTextView? = null
    private var etPassword: AutoCompleteTextView? = null
    private var tvForgotPass: TextView? = null
    private var btnLogin: Button? = null
    private var mAuth: FirebaseAuth? = null


    private var email: String? = null
    private var password: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
//        initializeId()

    }

//    private fun initializeId() {
//        etEmail = findViewById<View>(R.id.atvEmailLogin) as AutoCompleteTextView
//        etPassword = findViewById<View>(R.id.atvPasswordLogIn) as AutoCompleteTextView
//        tvForgotPass = findViewById<View>(R.id.tvForgotPass) as TextView
//        btnLogin = findViewById<View>(R.id.btnSignIn) as Button
//        mAuth = FirebaseAuth.getInstance()
//
//        btnLogin!!.setOnClickListener { loginUser() }
//        tvForgotPass!!.setOnClickListener{ForgorPassActivity()}
//
//    }
//    private fun ForgorPassActivity(){
////        var intent=Intent(this@LoginActivity,PasswordResetActivity::class.java)
////        startActivity(intent)
//    }

//    private fun loginUser() {
//        email = etEmail?.text.toString()
//        password = etPassword?.text.toString()
//        if (TextUtils.isEmpty(email)){
//            etEmail!!.setError("Please Enter Email")
//            return
//        }
//        if (TextUtils.isEmpty(password)){
//            etPassword!!.setError("Please Enter Password")
//            return
//
//        }
//        else{
//
//        mAuth!!.signInWithEmailAndPassword(email!!, password!!)
//                .addOnCompleteListener(this){task ->
//                if (task.isSuccessful){
//
//                    val intent=Intent(this@LoginActivity,MainActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                    startActivity(intent)
//                }
//                    else if (!task.isSuccessful){
//
//                    Toast.makeText(this,task.exception?.message,Toast.LENGTH_LONG).show()
//
//                }
//
//                }
//        }
//
//    }
}
