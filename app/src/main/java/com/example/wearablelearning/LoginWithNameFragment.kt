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
 * Login With Name Fragment.
 * One of two fragments called by LoginActivity. This fragment is associated to tab 0 (name).
 */
class LoginWithNameFragment : Fragment() {
    /**
     * Interface of onDataPass (for passing an array containing username and password to LoginActivity).
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
        return inflater.inflate(R.layout.fragment_login_with_name, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * edittext references where user inputs their name.
         */
        val edittext = view.findViewById<EditText>(R.id.loginNameEditText)

        //when user is typing their name into edittext
        edittext.addTextChangedListener(object : TextWatcher {

            /**
             * When text is being changed in edittext, pass array containing username to LoginActivity
             */
            override fun afterTextChanged(s: Editable) {
                dataPasser.onDataPass(arrayOf(edittext.text.toString()))
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {}
        })
    }
}