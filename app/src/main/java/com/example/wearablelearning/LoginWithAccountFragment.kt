package com.example.wearablelearning

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText


/**
 * Login With Account Fragment.
 * One of two fragments called by LoginActivity. This fragment is associated to tab 1 (credentials).
 */
class LoginWithAccountFragment : Fragment() {
    /**
     * Interface of onDataPass (for passing an array containing name to LoginActivity).
     */
    interface OnDataPass {
        fun onDataPass(data: Array<String>)
    }

    /**
     * OnDataPass initialized.
     */
    lateinit var dataPasser: OnDataPass

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as OnDataPass
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_with_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * edittextUser references where user inputs their username.
         */
        val editTextUser = view.findViewById<EditText>(R.id.loginUsernameEditText)

        /**
         * edittextPass references where user inputs their password.
         */
        val edittextPass = view.findViewById<EditText>(R.id.loginPasswordEditText)

        //when user is typing their username into edittext
        editTextUser.addTextChangedListener(object : TextWatcher {

            /**
             * When text is being changed in edittext, pass array containing name and password
             * to LoginActivity
             */
            override fun afterTextChanged(s: Editable) {
                dataPasser.onDataPass(arrayOf(editTextUser.text.toString(), edittextPass.text.toString()))
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        //when user is typing their password into edittext
        edittextPass.addTextChangedListener(object : TextWatcher {

            /**
             * When text is being changed in edittext, pass array containing name and password
             * to LoginActivity
             */
            override fun afterTextChanged(s: Editable) {
                dataPasser.onDataPass(arrayOf(editTextUser.text.toString(), edittextPass.text.toString()))
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }
}