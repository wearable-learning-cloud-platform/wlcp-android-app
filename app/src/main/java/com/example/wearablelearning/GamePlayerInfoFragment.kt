package com.example.wearablelearning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * The [GamePlayerInfoFragment] class is called by [GameActivity].
 * This is one of four fragments called by [GameActivity] and is associated with
 * [R.layout.fragment_game_player_info]. It is the topmost fragment that displays name, player, and
 * team values.
 */
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

        /**
         * The _gameInfo_ is a [GameInfo] object and is used to track user input about the game
         * (e.g., gamePin, name, etc. - See the [GameInfo] class for all relevant fields).
         * All [GameInfo] variables are null at startup and get populated as the user moves
         * through the different app Activities; [GameInfo] objects are passed from one Activity
         * to the next.
         */
        val gameInfo = intent.getSerializableExtra("gameInfo") as? GameInfo

        /** The _textViewGameName_ is used to display the game's name. */
        val textViewGameName = view.findViewById<TextView>(R.id.game_name_tv)

        /** The _textViewPlayerName_ is used to display the player's name. */
        val textViewPlayerName = view.findViewById<TextView>(R.id.player_name_tv)

        /** The _textViewTeamName_ is used to display the team's name. */
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

        if (gameInfo?.team != null && gameInfo.player != null) {
            textViewTeamName.text = " (" + gameInfo.team + " " + gameInfo.player + ")"
        }
    }
}