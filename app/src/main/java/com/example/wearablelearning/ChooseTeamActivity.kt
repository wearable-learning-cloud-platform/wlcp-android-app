package com.example.wearablelearning

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

/**
 * The [ChooseTeamActivity] class is launched from [LoginActivity] and is used to request the
 * player's Team number and Player number.
 * This activity launches [GameActivity] on valid user input.
 */
class ChooseTeamActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_team)

        val teamArr = getDropdownList("team")
        val playerArr = getDropdownList("player")

        val spinnerTeam: Spinner = findViewById(R.id.team_spinner)
        val adapterTeam = ArrayAdapter(this, android.R.layout.simple_spinner_item, teamArr)
        adapterTeam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTeam.adapter = adapterTeam

        val spinnerPlayer: Spinner = findViewById(R.id.player_spinner)
        val adapterPlayer = ArrayAdapter(this, android.R.layout.simple_spinner_item, playerArr)
        adapterPlayer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPlayer.adapter = adapterPlayer

        /**
         * Retrieve the [GameInfo] object from the intent that started this Activity.
         *
         * The _gameInfo_ is a [GameInfo] object and is used to track user input about the game
         * (e.g., gamePin, name, etc. - See the [GameInfo] class for all relevant fields).
         */
        val gameInfo = intent.getSerializableExtra("gameInfo") as? GameInfo
        var gameInfoOfStartedGame = intent.getSerializableExtra("gameInfoOfStartedGame") as? GameInfo

        if(gameInfo?.team != null && gameInfo.player != null) {
            spinnerTeam.setSelection(teamArr.indexOf(
                teamArr.first { elem -> elem == gameInfo.team }
            ))

            spinnerPlayer.setSelection(playerArr.indexOf(
                playerArr.first { elem -> elem == gameInfo.player }
            ))
        }

        /**
         * The 'Join Team' button that triggers a switch from [ChooseTeamActivity] to [GameActivity].
         */
        val joinTeamBtn: Button = findViewById(R.id.join_game_btn)

        /** Set the _joinTeamBtn_ listener to switch from [ChooseTeamActivity] to [GameActivity]. */
        joinTeamBtn.setOnClickListener {

            val teamSelected: String = spinnerTeam.selectedItem.toString()
            val playerSelected: String = spinnerPlayer.selectedItem.toString()

            val tempMsg = resources.getString(R.string.confirm_join_text)
            var msg = tempMsg.substringBefore(" Team x Player y?")
            msg = "$msg $teamSelected $playerSelected?"

            /** Display a dialog box to confirm the user's Team and Player number selections. */
            MaterialAlertDialogBuilder(this, R.style.Theme_WearableLearning_AlertDialog)
                .setMessage(msg)
                .setNegativeButton(resources.getString(R.string.no_text)) { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton(resources.getString(R.string.yes_text)) { _, _ ->

                    /**
                     * Set the _team_ and _player_ fields of the [GameInfo] object being
                     * tracked in this activity.
                     */
                    if (gameInfo != null) {
                        setGameInfoTeamAndPlayer(teamSelected, playerSelected, gameInfo)
                    }

                    /** Build _intent_ to switch from [ChooseTeamActivity] to [GameActivity]. */
                    val intent = Intent(this@ChooseTeamActivity, GameActivity::class.java)

                    /** Add the [GameInfo] objects into _intent_ */
                    intent.putExtra("gameInfo", gameInfo)
                    intent.putExtra("gameInfoOfStartedGame", gameInfoOfStartedGame)

                    /** Launch [GameActivity] */
                    startActivity(intent)
                }
                .show()
        }

        val backBtn: Button = findViewById(R.id.back_btn)

        backBtn.setOnClickListener {
            if (gameInfo != null) {
                val teamSelected: String = spinnerTeam.selectedItem.toString()
                val playerSelected: String = spinnerPlayer.selectedItem.toString()

                /**
                 * Set the _team_ and _player_ fields of the [GameInfo] object being
                 * tracked in this activity.
                 */
                setGameInfoTeamAndPlayer(teamSelected, playerSelected, gameInfo)
            }

            val intent = Intent(this@ChooseTeamActivity, LoginActivity::class.java)
            intent.putExtra("gameInfo", gameInfo)
            startActivity(intent)
        }
    }


    /**
     * The [setGameInfoTeamAndPlayer] utility function sets the _team_ and _player_ info of the
     * [GameInfo] object being tracked in this activity.
     * @param [teamSelected] The team number of the current player for the current game
     * @param [playerSelected] The player number of the current player for the current game
     */
    private fun setGameInfoTeamAndPlayer(teamSelected: String, playerSelected: String, gameInfo: GameInfo) {
        if (gameInfo != null) {
            gameInfo.team = teamSelected
            gameInfo.player = playerSelected
        }
    }


    /**
     * The [getDropdownList] utility function produces a collection of values to populate a
     * related dropdown list with - i.e., the _Team_ dropdown list or _Player_ dropdown list of
     * the [ChooseTeamActivity] screen.
     * @param [item] This is either "team" or "player"
     * @return An ArrayList of values to be used to populate the Team or Player dropdowns
     */
    private fun getDropdownList(item: String): ArrayList<String> {

        /** Switch the first character of [item] to uppercase */
        val itemCap = item.replaceFirstChar{
            if (it.isLowerCase())
                it.titlecase(Locale.getDefault())
            else
                it.toString()
        }

        /** _arr_ is an ArrayList object that will contain the dropdown values */
        val arr = ArrayList<String>()

        /** Populate _arr_ with the dropdown values */
        for (i in 1..mapCount(item)) {
            arr.add("$itemCap $i")
        }

        return arr
    }


    /**
     * The [mapCount] utility function returns the number of teams/players depending on which
     * of "team" or "player" for the [item] parameter was passed.
     * @param [item] This is either "team" or "player"
     * @return The Int value of either team or player for the current game
     */
    private fun mapCount(item: String): Int {

        val jsonContents: String = resources.openRawResource(R.raw.game12) //TODO
            .bufferedReader().use { it.readText() }

        val gameJSONInfo: String = StringUtils.parseJsonWithGson(jsonContents).getValue("game").toString()
        var cnt = "1"

        /** Get the value of team_cnt in the JSON */
        if(item == "team")
            cnt = gameJSONInfo.substringAfter("team_cnt=").substringBefore(", players_per_team=")
        /** Get the value of players_per_team in the JSON */
        else if(item == "player")
            cnt = gameJSONInfo.substringAfter("players_per_team=").substringBefore(", visibility=")

        return cnt.split(".")[0].toInt()
    }
}