package com.example.wearablelearning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner

class ChooseTeamActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_team)

        val spinner: Spinner = findViewById(R.id.spinner)
        val adapter = ArrayAdapter.createFromResource(this, R.array.team_player_list, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val joinTeamBtn: Button = findViewById(R.id.join_game_btn)
        val backBtn: Button = findViewById(R.id.back_btn)

        joinTeamBtn.setOnClickListener {
            val intent = Intent(this@ChooseTeamActivity, GameActivity::class.java)
            startActivity(intent)
        }

        backBtn.setOnClickListener {
            val intent = Intent(this@ChooseTeamActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}