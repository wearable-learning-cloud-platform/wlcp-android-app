package com.example.wearablelearning

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class ChooseTeamActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_team)

        val spinner: Spinner = findViewById(R.id.spinner)
        val adapter = ArrayAdapter.createFromResource(this, R.array.team_player_list, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val gameInfo = intent.getSerializableExtra("gameInfo") as? GameInfo

        val joinTeamBtn: Button = findViewById(R.id.join_game_btn)

        joinTeamBtn.setOnClickListener {
            if (gameInfo != null) {
                setGameInfoTeamAndPlayer(spinner, gameInfo)
            }

            val intent = Intent(this@ChooseTeamActivity, GameActivity::class.java)
            intent.putExtra("gameInfo", gameInfo)
            startActivity(intent)
        }

        val backBtn: Button = findViewById(R.id.back_btn)

        backBtn.setOnClickListener {
            if (gameInfo != null) {
                setGameInfoTeamAndPlayer(spinner, gameInfo)
            }

            val intent = Intent(this@ChooseTeamActivity, LoginActivity::class.java)
            intent.putExtra("gameInfo", gameInfo)
            startActivity(intent)
        }
    }

    private fun setGameInfoTeamAndPlayer(spinner: Spinner, gameInfo: GameInfo) {
        val spinnerSelected: String = spinner.selectedItem.toString()
        val selected: Array<String> = parseSpinnerSelection(spinnerSelected)

        if (gameInfo != null) {
            gameInfo.team = selected[0]
            gameInfo.player = selected[1]
        }
    }

    private fun parseSpinnerSelection(str: String): Array<String> {
        val strSplit: List<String> = str.split(" ")

        return arrayOf(strSplit[1], strSplit[3])
    }
}