package com.example.wearablelearning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class GamePlayerInfoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_player_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val intent = requireActivity().intent
        var gameInfo = intent.getSerializableExtra("gameInfo") as? GameInfo

        val textViewGameName = view.findViewById<TextView>(R.id.game_name_tv)
        val textViewPlayerName = view.findViewById<TextView>(R.id.player_name_tv)
        val textViewTeamName = view.findViewById<TextView>(R.id.team_num_tv)

        if (gameInfo?.gameName != null) {
            textViewGameName.text = gameInfo.gameName
        }

        if (gameInfo?.name != null) {
            textViewPlayerName.text = gameInfo.name
        }
        else if (gameInfo?.userName != null) {
            textViewPlayerName.text = gameInfo.userName
        }

        if (gameInfo?.team != null && gameInfo?.player != null) {
            textViewTeamName.text = " (Team " + gameInfo.team + " Player " + gameInfo.player + ")"
        }
    }
}