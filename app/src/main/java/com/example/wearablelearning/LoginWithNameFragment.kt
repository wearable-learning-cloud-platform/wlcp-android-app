package com.example.wearablelearning

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import java.util.*


/**
 * Login With Name Fragment.
 * One of two fragments called by LoginActivity. This fragment is associated to tab 0 (name).
 */
class LoginWithNameFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_with_name, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val intent = requireActivity().intent
        var gameInfo = intent.getSerializableExtra("gameInfo") as? GameInfo

        val editTextName = view?.findViewById<EditText>(R.id.loginNameEditText)

        if (gameInfo?.name != null && editTextName != null) {
            editTextName.setText(gameInfo.name)
        }
    }
}