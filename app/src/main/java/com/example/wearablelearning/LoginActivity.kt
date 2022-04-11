package com.example.wearablelearning

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import java.util.*


/**
 * The [LoginActivity] class is launched from [MainActivity] and is used to request the player's
 * name (for users with no WearableLearning account) or username and password (for users with a
 * WearableLearning account). This activity launches [ChooseTeamActivity] on valid user input.
 */
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /**
         * Retrieve the [GameInfo] object from the intent that started this Activity.
         *
         * The _gameInfo_ is a [GameInfo] object and is used to track user input about the game
         * (e.g., gamePin, name, etc. - See the [GameInfo] class for all relevant fields).
         */
        val gameInfo = intent.getSerializableExtra("gameInfo") as? GameInfo
        var gameInfoOfStartedGame = intent.getSerializableExtra("gameInfoOfStartedGame") as? GameInfo

        /**
         * The 'Join Game' button that triggers a switch from [LoginActivity] to [ChooseTeamActivity].
         */
        val joinGameBtn: Button = findViewById(R.id.join_game_btn)

        /**
         * The 'Back' button that triggers a switch from [LoginActivity] to [MainActivity].
         */
        val backBtn: Button = findViewById(R.id.back_btn)

        /**
         * _tabPos_ is the integer value of the index position of a tab (leftmost tab is 0).
         */
        var tabPos = 0

        /**
         * _tabLayout_ references tabLayout of activity.
         */
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)

        /**
         * _fm_ is the fragment manager.
         */
        val fm: FragmentManager = supportFragmentManager

        /**
         * _ft_ is the fragment transaction.
         */
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.replace(R.id.frameLayout, LoginWithNameFragment())
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.commit()

        /**
         * Inflate [R.layout.fragment_login_with_account] if the user previously logged in with
         * a WearableLearning account username and password.
         */
        if(gameInfo?.userName != null) {
            val tab = tabLayout.getTabAt(1)
            tab?.select()

            val ft: FragmentTransaction = fm.beginTransaction()
            ft.replace(R.id.frameLayout, LoginWithAccountFragment())
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            ft.commit()
        }

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

        /** Set the _joinGameBtn_ listener to switch from [LoginActivity] to [ChooseTeamActivity]. */
        joinGameBtn.setOnClickListener {

            /**
             * _inputs_ is an array of the user entries retrieved from the fragment associated
             * with the active tab. _inputs_ has a size of 1 if _tabPos_=0 (name login), and 2 if
             * _tabPos_=1 (WearableLearning account username and password login).
             */
            val inputs: Array<String> = getLoginInputs(tabPos)
            val name = inputs[0].trim()

            /** Check whether the user joined with their name or WearableLearning credentials. */
            if(checkInput(inputs, tabPos)) {

                /** User joined the game with their name */
                if (gameInfo != null && tabPos == 0) {
                    gameInfo.name = name
                    gameInfo.userName = null
                }
                /** User joined the game with their WearableLearning credentials */
                else if(gameInfo != null && tabPos == 1) {
                    gameInfo.name = null
                    gameInfo.userName = name
                }

                val tempMsg = resources.getString(R.string.confirm_login_text)
                var msg = tempMsg.substringBefore(" Username?")
                msg = "$msg $name?"

                /** Display a dialog box to confirm the user's name/credential entries. */
                MaterialAlertDialogBuilder(this, R.style.Theme_WearableLearning_AlertDialog)
                    .setMessage(msg)
                    .setNegativeButton(resources.getString(R.string.no_text)) { dialog, _ ->
                        dialog.cancel()
                    }
                    .setPositiveButton(resources.getString(R.string.yes_text)) { _, _ ->
                        /**
                         * The intent to switch activities from [LoginActivity] to [ChooseTeamActivity].
                         */
                        val intent = Intent(this@LoginActivity, ChooseTeamActivity::class.java)

                        /** Add the [GameInfo] objects into _intent_ */
                        intent.putExtra("gameInfo", gameInfo)
                        intent.putExtra("gameInfoOfStartedGame", gameInfoOfStartedGame)

                        /** Launch [ChooseTeamActivity] */
                        startActivity(intent)
                    }
                    .show()
            }
        }

        /** Set the _backBtn_ listener to switch from [LoginActivity] to [MainActivity]. */
        backBtn.setOnClickListener{

            /** The intent to switch activities from [LoginActivity] to [MainActivity]. */
            val intent = Intent(this@LoginActivity, MainActivity::class.java)

            /** Add the [GameInfo] objects into _intent_ */
            intent.putExtra("gameInfo", gameInfo)

            /** Launch [MainActivity] */
            startActivity(intent)
        }
    }

    /**
     * Retrieves inputs from the editTexts of a fragment. Fragment depends on the tabPos.
     * @param tabPos An int which is either 0 (name login) or 1 (account login)
     */
    private fun getLoginInputs(tabPos: Int): Array<String> {
        val inputs: Array<String> = arrayOf("", "")

        //leftmost tab: Name
        if(tabPos == 0) {
            val editTextName: EditText = findViewById(R.id.loginNameEditText)

            inputs[0] = editTextName.text.toString()
        }
        //second tab: WearableLearning.org credentials
        else if(tabPos == 1) {
            val editTextUsername: EditText = findViewById(R.id.loginUsernameEditText)
            val editTextPassword: EditText = findViewById(R.id.loginPasswordEditText)

            inputs[0] = editTextUsername.text.toString()
            inputs[1] = editTextPassword.text.toString()
        }

        return inputs
    }

    /**
     * Checks the values of name (inputs[0]; tab 0 selected) or username and password (inputs[0] and
     * inputs[1]; tab 1 selected). All must have at least 1 character. Username and password must
     * match json. When invalid input, sets error messages.
     * @param inputs: Array<String> of inputs from EditText(s) of fragment
     * @param tabPos: integer
     */
    private fun checkInput(inputs: Array<String>, tabPos: Int): Boolean {
        //left tab (name)
        if(tabPos == 0) {
            if(StringUtils.isEmptyOrBlank(inputs[0])) {
                val errorText: TextView = findViewById(R.id.error_tv2)
                errorText.text = getString(R.string.new_name_missing_error)
                errorText.visibility = TextView.VISIBLE
                return false
            }
        }
        //right tab (WearableLearning.org account)
        else if(tabPos == 1) {
            val editTextUsername: EditText = findViewById(R.id.loginUsernameEditText)
            val editTextPassword: EditText = findViewById(R.id.loginPasswordEditText)

            //no username or password
            if(StringUtils.isEmptyOrBlank(inputs[0]) and StringUtils.isEmptyOrBlank(inputs[1])) {
                val errorText: TextView = findViewById(R.id.error_tv3)
                errorText.text = getString(R.string.username_missing_error)
                errorText.visibility = TextView.VISIBLE
                return false
            }
            //username but no password
            else if(StringUtils.isEmptyOrBlank(inputs[1])) {
                val errorText: TextView = findViewById(R.id.error_tv3)
                errorText.text = getString(R.string.password_missing_error)
                errorText.visibility = TextView.VISIBLE
                return false
            }
            //password but no username
            else if(StringUtils.isEmptyOrBlank(inputs[0])) {
                val errorText: TextView = findViewById(R.id.error_tv3)
                errorText.text = getString(R.string.username_missing_error)
                errorText.visibility = TextView.VISIBLE
                return false
            }

            //username and password input but are invalid
            if(!StringUtils.isEmptyOrBlank(inputs[0]) and !StringUtils.isEmptyOrBlank(inputs[1]) and !isValidCredentials(inputs[0], inputs[1])) {
                val errorText: TextView = findViewById(R.id.error_tv3)
                errorText.text = getString(R.string.username_password_error)
                errorText.visibility = TextView.VISIBLE
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
     * @param [username] String username from editTextName
     * @param [password] String password from editTextPassword
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