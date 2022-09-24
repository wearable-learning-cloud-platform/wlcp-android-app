package com.example.wearablelearning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

/**
 * The [LoginWithAccountFragment] class is called by [LoginActivity].
 * This is one of two fragments called by [LoginActivity] and is associated with
 * [R.layout.fragment_login_with_account] (or tab 1) for logging into the WearableLearning
 * app with WearableLearning credentials (username, password).
 */
class LoginWithAccountFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_with_account, container, false)
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

        /** The _editTextName_ is used to allow the user to enter a username. */
        val editTextName = view.findViewById<EditText>(R.id.loginUsernameEditText)

        if (gameInfo?.userName != null && editTextName != null) {
            editTextName.setText(gameInfo.userName)
        }
    }
}