package com.example.wearablelearning

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList

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

        val gameInfo = intent.getSerializableExtra("gameInfo") as? GameInfo

        val joinTeamBtn: Button = findViewById(R.id.join_game_btn)

        joinTeamBtn.setOnClickListener {
            val teamSelected: String = spinnerTeam.selectedItem.toString()
            val playerSelected: String = spinnerPlayer.selectedItem.toString()

            val tempMsg = resources.getString(R.string.confirm_join_text)
            var msg = tempMsg.substringBefore(" Team x Player y?")
            msg = "$msg $teamSelected $playerSelected?"

            MaterialAlertDialogBuilder(this, R.style.Theme_WearableLearning_AlertDialog)
                .setMessage(msg)
                .setNegativeButton(resources.getString(R.string.no_text)) { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton(resources.getString(R.string.yes_text)) { _, _ ->
                    if (gameInfo != null) {
                        setGameInfoTeamAndPlayer(teamSelected, playerSelected, gameInfo)
                    }

                    val intent = Intent(this@ChooseTeamActivity, GameActivity::class.java)
                    intent.putExtra("gameInfo", gameInfo)
                    startActivity(intent)
                }
                .show()
        }

        val backBtn: Button = findViewById(R.id.back_btn)

        backBtn.setOnClickListener {
            if (gameInfo != null) {
                val teamSelected: String = spinnerTeam.selectedItem.toString()
                val playerSelected: String = spinnerPlayer.selectedItem.toString()

                setGameInfoTeamAndPlayer(teamSelected, playerSelected, gameInfo)
            }

            val intent = Intent(this@ChooseTeamActivity, LoginActivity::class.java)
            intent.putExtra("gameInfo", gameInfo)
            startActivity(intent)
        }
    }

    private fun setGameInfoTeamAndPlayer(teamSelected: String, playerSelected: String, gameInfo: GameInfo) {
        if (gameInfo != null) {
            gameInfo.team = teamSelected
            gameInfo.player = playerSelected
        }
    }

    private fun getDropdownList(item: String): ArrayList<String> {
        val arr = ArrayList<String>()
        val itemCap = item.replaceFirstChar{
            if (it.isLowerCase())
                it.titlecase(Locale.getDefault())
            else
                it.toString()
        }

        for (i in 1..mapCount(item)) {
            arr.add("$itemCap $i")
        }

        return arr
    }

    private fun mapCount(item: String): Int {
        val jsonContents: String = resources.openRawResource(R.raw.game12) //TODO
            .bufferedReader().use { it.readText() }
        val gameJSONInfo: String = StringUtils.parseJsonWithGson(jsonContents).getValue("game").toString()
        var cnt = "1"

        if(item == "team")
            cnt = gameJSONInfo.substringAfter("team_cnt=").substringBefore(", players_per_team=")
        else if(item == "player")
            cnt = gameJSONInfo.substringAfter("players_per_team=").substringBefore(", visibility=")

        return cnt.split(".")[0].toInt()
    }
}