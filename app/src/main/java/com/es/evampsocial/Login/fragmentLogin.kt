package com.es.evampsocial.Login


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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_fragment_login.*


class fragmentLogin : Fragment() {
    var mAuth: FirebaseAuth? = null
    var mFirestore: FirebaseFirestore? = null
    var email: String? = null
    var password: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance()
        mFirestore = FirebaseFirestore.getInstance()

        super.onViewCreated(view, savedInstanceState)
        tvSignUp.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.to_signUpFragment))
        btnSignIn.setOnClickListener(View.OnClickListener { loginUser() })
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            var intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            atvEmailLogin!!.visibility = View.INVISIBLE
        }

    }

    private fun loginUser() {
        email = atvEmailLogin?.text.toString()
        password = atvPasswordLogIn?.text.toString()
        if (TextUtils.isEmpty(email)) {
            atvEmailLogin!!.setError("Please Enter Email")
            return
        }
        if (TextUtils.isEmpty(password)) {
            atvPasswordLogIn!!.setError("Please Enter Password")
            return

        } else {

            mAuth!!.signInWithEmailAndPassword(email!!, password!!)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            val intent = Intent(context, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                        } else if (!task.isSuccessful) {

                            Toast.makeText(context, task.exception?.message, Toast.LENGTH_LONG).show()


                        }

                    }
        }

    }
}
