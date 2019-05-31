package com.es.evampsocial.Registration

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.es.evampsocial.R
import kotlinx.android.synthetic.main.fragment_sign_up.*


class SignUpFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSignUpReg.setOnClickListener{
            val nameBundle=Bundle()
            nameBundle.putString("fnameArg",atvFirstName.text.toString())
            nameBundle.putString("lastNameArg",atvLastName.text.toString())
            it.findNavController().navigate(R.id.signUpEmailFragment,nameBundle)

        }
    }
}
