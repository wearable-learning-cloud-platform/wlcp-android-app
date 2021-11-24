package com.example.wearablelearning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.tabs.TabLayout
import java.util.*


/**
 * Login activity.
 * This class is the second screen of the app, asking the player for their name or username and password.
 * On valid inputs, activity switches to GameActivity.
 */
class LoginActivity : AppCompatActivity(), LoginWithNameFragment.OnDataPass, LoginWithAccountFragment.OnDataPass {
    /**
     * Companion objects include tabPos (tab position), name, username, and password.
     */
    companion object {
        /**
         * tabPos is the integer value of the index position of a tab (leftmost tab is 0).
         */
        var tabPos: Int = -1

        /**
         * name is the user-inputted name on tab 0.
         */
        var name: String = ""

        /**
         * username is the user-inputted username on tab 1.
         */
        var username: String = ""

        /**
         * password is the user-inputted password on tab 1.
         */
        var password: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /**
         * tabLayout references tabLayout of activity.
         */
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)

        /**
         * fm is the fragment manager.
         */
        val fm: FragmentManager = supportFragmentManager

        /**
         * ft is the fragment transaction.
         */
        val ft: FragmentTransaction = fm.beginTransaction()

        ft.replace(R.id.frameLayout, LoginWithNameFragment())
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.commit()

        //when tab is changed by user
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    tabPos = tab.position

                    //leftmost tab: Name
                    if (tabPos == 0) {
                        val ft: FragmentTransaction = fm.beginTransaction()
                        ft.replace(R.id.frameLayout, LoginWithNameFragment())
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        ft.commit()
                    }
                    //second tab: WearableLearning.org credentials
                    else if (tabPos == 1) {
                        val ft: FragmentTransaction = fm.beginTransaction()
                        ft.replace(R.id.frameLayout, LoginWithAccountFragment())
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        ft.commit()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        /**
         * The 'Begin Game' button that triggers a switch from LoginActivity to GameActivity.
         */
        val button: Button = findViewById(R.id.button)

        //when user selects 'Begin game' button
        button.setOnClickListener {
            if(checkInput(name, username, password)) {
                /**
                 * The intent to switch activities from LoginActivity to GameActivity.
                 */
                val intent = Intent(this@LoginActivity, GameActivity::class.java)

                startActivity(intent)
            }
        }
    }

    /**
     * Retrieves the inputs from a tab/fragment as a string array and sets the companion object
     * variables to the values. If tab 0/Name, array only has length 1. Otherwise, length 2.
     * @param data: string array of size 1 or 2
     */
    override fun onDataPass(data: Array<String>) {
        name = data[0]

        //if tab 1/WearableLearning.org credentials
        if(data.size > 1) {
            name = ""
            username = data[0]
            password = data[1]
        }
    }

    /**
     * Checks the values of name (tab 0 selected) or username and password (tab 1 selected).
     * All must have at least 1 character. Username and password must match json. When invalid input,
     * sets error messages.
     * @param name: string text
     * @param username: string text
     * @param password: string text
     */
    private fun checkInput(name: String, username: String, password: String): Boolean {
        //left tab (name)
        if(tabPos == 0) {
            val editTextName: EditText = findViewById(R.id.loginNameEditText)

            if(StringUtils.isEmptyOrBlank(name)) {
                editTextName.error = getString(R.string.new_name_missing_error)
                return false
            }
        }
        //right tab (WearableLearning.org account)
        else if(tabPos == 1) {
            val editTextUsername: EditText = findViewById(R.id.loginUsernameEditText)
            val editTextPassword: EditText = findViewById(R.id.loginPasswordEditText)

            //no username or password
            if(StringUtils.isEmptyOrBlank(username) and StringUtils.isEmptyOrBlank(password)) {
                editTextUsername.error = getString(R.string.username_missing_error)
                editTextPassword.error = getString(R.string.password_missing_error)
                return false
            }
            //username but no password
            else if(StringUtils.isEmptyOrBlank(password)) {
                editTextUsername.error = null
                editTextPassword.error = getString(R.string.password_missing_error)
                return false
            }
            //password but no username
            else if(StringUtils.isEmptyOrBlank(password)) {
                editTextUsername.error = getString(R.string.username_missing_error)
                editTextPassword.error = null
                return false
            }

            //username and password input but are invalid
            if(!StringUtils.isEmptyOrBlank(username) and !StringUtils.isEmptyOrBlank(password) and !isValidCredentials(username, password)) {
                editTextUsername.error = getString(R.string.username_password_error)
                editTextPassword.error = getString(R.string.username_password_error)
                return false
            }
        }
        // invalid tabPos (should never enter else)
        else {
            return false
        }

        return true
    }

    /**
     * Checks if username and password pair are valid by comparing to map of credentials from json.
     * @param username: string username from editTextName
     * @param password: string password from editTextPassword
     * @return true if valid
     */
    private fun isValidCredentials(username: String, password: String): Boolean {
        /**
         * Map of <username, password> from json in res/raw.
         */
        val map = getStoredCredentials()

        if(map.containsKey(username) && map[username] == password) {
            return true
        }

        return false
    }

    /**
     * Pulls all login credentials from json and outputs them as a map of <username, password> pairs.
     * @return map of <username, password> pairs
     */
    private fun getStoredCredentials(): MutableMap<String, String> {
        /**
         * Json file converted to string.
         */
        val jsonContents: String = resources.openRawResource(R.raw.data)
            .bufferedReader().use { it.readText() }
            .filter { !it.isWhitespace() }

        /**
         * List of usernames from json file.
         */
        val usernames = Regex("username\"[:]\"(.*?)[\"]").findAll(jsonContents).map { it.groupValues[1] }.toList()

        /**
         * List of passwords from json file.
         */
        val passwords = Regex("password\"[:]\"(.*?)[\"]").findAll(jsonContents).map { it.groupValues[1] }.toList()

        /**
         * Two iterators: one for usernames list and one for passwords list.
         */
        val i1: Iterator<String> = usernames.iterator()
        val i2: Iterator<String> = passwords.iterator()

        /**
         * Mapping of <username, password> pairs.
         */
        val map : MutableMap<String, String> = mutableMapOf()

        while(i1.hasNext() || i2.hasNext()) {
            map[i1.next()] = i2.next()
        }

        return map
    }
}