package com.example.wearablelearning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.io.*

/**
 * The [MainActivity] class is the app entry point and displays the first screen on launch
 * (i.e., the 'welcome/enter a game pin' screen).
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** The _gamePinEditText_ is used to accept a user-input number for the game PIN. */
        val gamePinEditText: EditText = findViewById(R.id.editText)

        /** The _gamePinErrorText_ is used to display errors on incorrect game PIN input. */
        val gamePinErrorText: TextView = findViewById(R.id.error_tv)

        /**
         * Set the _gamePinEditText_ listener to hide error messages when a game PIN is
         * being entered.
         */
        gamePinEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                gamePinErrorText.visibility = TextView.INVISIBLE
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })


        /**
         * The _joinGameButton_ is used to trigger switching from [MainActivity] to [LoginActivity].
         * @TODO Set the [R.id.join_game_btn] text to the actual string resource
         */
        val joinGameButton: Button = findViewById(R.id.join_game_btn)

        /** Set the _joinGameButton_ listener to switch activities when the game PIN is correct. */
        joinGameButton.setOnClickListener {

            /** Launch [LoginActivity] if the user-input game PIN is correct. */
            if (checkInput(gamePinEditText, gamePinErrorText)) {

                /** Build _intentMainToLogin_ to switch from [MainActivity] to [LoginActivity]. */
                val intentMainToLogin = Intent(this@MainActivity, LoginActivity::class.java)

                startActivity(intentMainToLogin)
            }
        }
    }


    /**
     * The [checkInput] utility function checks if the user input value of the edit text is valid.
     * @param [editText] The EditText element used to accept a user-input number for the game PIN.
     * @param [errorText] The TextView element used to display errors on incorrect game PIN input.
     * @return true if valid
     */
    private fun checkInput(editText: EditText, errorText: TextView): Boolean {

        /** The string-converted user-input game PIN value of [editText]. */
        val gamePinUserInput: String = editText.text.toString()

        /** An array of valid game PINs from the [R.raw.data] JSON for validating input. */
        val validInputs: List<String> = StringUtils.getValuesFromJSON(resources, R.raw.data, "game_pins")

        /** Error: If the game PIN is missing, show [R.string.game_pin_missing_error]. */
        if (StringUtils.isEmptyOrBlank(gamePinUserInput)) {
            errorText.text = getString(R.string.game_pin_missing_error)
            errorText.visibility = TextView.VISIBLE
            return false
        }
        /** Error: If the game PIN is incorrect, show [R.string.game_pin_invalid_error]. */
        else if (!StringUtils.isStringInList(gamePinUserInput, validInputs)) {
            errorText.text = getString(R.string.game_pin_invalid_error)
            errorText.visibility = TextView.VISIBLE
            return false
        }

        return true
    }
}