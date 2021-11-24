package com.example.wearablelearning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.EditText
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
        val button: Button = findViewById(R.id.button)

        /**
         * The edit text that accepts a number as the game pin.
         */
        val editText: EditText = findViewById(R.id.editText)

        button.setOnClickListener {
            if(checkInput(editText)) {
                /**
                 * The intent to switch activities from MainActivity to LoginActivity.
                 */
                val intent = Intent(this@MainActivity, LoginActivity::class.java)

                startActivity(intent)
            }
        }
    }

    /**
     * Checks if the inputted value of the edit text is valid.
     * @param editText: EditText
     * @return true if valid
     */
    private fun checkInput(editText: EditText): Boolean {
        /**
         * The value of editText converted to a string.
         */
        val input: String = editText.text.toString()

        /**
         * An array of valid inputs from json for validating input.
         */
        val validInputs: List<String> = StringUtils.getValuesFromJSON(resources, R.raw.data, "game_pins")

        if(StringUtils.isEmptyOrBlank(input)) {
            editText.error = getString(R.string.game_pin_missing_error)
            return false
        }
        else if(!StringUtils.isStringInList(input, validInputs)) {
            editText.error = getString(R.string.game_pin_invalid_error)
            return false
        }

        return true
    }
}