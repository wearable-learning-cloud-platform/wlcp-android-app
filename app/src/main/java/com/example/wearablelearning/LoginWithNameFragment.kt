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
 * The [LoginWithNameFragment] class is called by [LoginActivity].
 * This is one of two fragments called by [LoginActivity] and is associated with
 * [R.layout.fragment_login_with_name] (or tab 0) for logging into the
 * WearableLearning app with a name.
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