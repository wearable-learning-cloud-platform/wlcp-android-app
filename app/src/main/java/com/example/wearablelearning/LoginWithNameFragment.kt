package com.example.wearablelearning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment

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

        /**
         * The _gameInfo_ is a [GameInfo] object and is used to track user input about the game
         * (e.g., gamePin, name, etc. - See the [GameInfo] class for all relevant fields).
         * All [GameInfo] variables are null at startup and get populated as the user moves
         * through the different app Activities; [GameInfo] objects are passed from one Activity
         * to the next.
         */
        val intent = requireActivity().intent
        val gameInfo = intent.getSerializableExtra("gameInfo") as? GameInfo

        /** The _editTextName_ is used to allow the user to enter a name. */
        val editTextName = view.findViewById<EditText>(R.id.loginNameEditText)

        if (gameInfo?.name != null && editTextName != null) {
            editTextName.setText(gameInfo.name)
        }
    }
}