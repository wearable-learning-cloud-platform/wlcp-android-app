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
 * Main activity.
 * This class is the main activity of the app and displays the first screen on launch (the 'welcome/
 * enter a game pin' screen).
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * The 'Join Game' button that triggers a switch from MainActivity to LoginActivity.
         */
        val button: Button = findViewById(R.id.join_game_btn)

        /**
         * The edit text that accepts a number as the game pin.
         */
        val editText: EditText = findViewById(R.id.editText)

        /**
         * The error text that displays only if input is incorrect.
         */
        val errorText: TextView = findViewById(R.id.error_tv)

        // When Join Game button is selected, check editText input and, if correct, switch activities.
        button.setOnClickListener {
            if(checkInput(editText, errorText)) {
                /**
                 * The intent to switch activities from MainActivity to LoginActivity.
                 */
                val intent = Intent(this@MainActivity, LoginActivity::class.java)

                startActivity(intent)
            }
        }

        // When a gamepin is being entered, hide all error messages.
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                errorText.visibility = TextView.INVISIBLE
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    /**
     * Checks if the inputted value of the edit text is valid.
     * @param editText: EditText
     * @return true if valid
     */
    private fun checkInput(editText: EditText, errorText: TextView): Boolean {
        /**
         * The value of editText converted to a string.
         */
        val input: String = editText.text.toString()

        /**
         * An array of valid inputs from json for validating input.
         */
        val validInputs: List<String> = StringUtils.getValuesFromJSON(resources, R.raw.data, "game_pins")

        // Gamepin missing error.
        if(StringUtils.isEmptyOrBlank(input)) {
            errorText.text = getString(R.string.game_pin_missing_error)
            errorText.visibility = TextView.VISIBLE
            return false
        }
        // Gamepin incorrect error.
        else if(!StringUtils.isStringInList(input, validInputs)) {
            errorText.text = getString(R.string.game_pin_invalid_error)
            errorText.visibility = TextView.VISIBLE
            return false
        }

        return true
    }
}