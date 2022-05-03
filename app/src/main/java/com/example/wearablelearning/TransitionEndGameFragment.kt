package com.example.wearablelearning

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class TransitionEndGameFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transition_end_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gameIntent = requireActivity().intent
        val gameInfo = gameIntent.getSerializableExtra("gameInfo") as? GameInfo

        val button = view.findViewById<Button>(R.id.end_game_btn)

        button.setOnClickListener {
            if (gameInfo != null) {
                gameInfo.interactionType = "endOfGameButton"

                activity?.let { activity -> LogUtils.logGamePlay("player", gameInfo, true, activity.applicationContext) }
                activity?.let { activity -> LogUtils.logGamePlay("gamePlay", gameInfo, true, activity.applicationContext) }
            }

            val intent = Intent(activity, LoginActivity::class.java)
            intent.putExtra("gameInfo", gameInfo)
            startActivity(intent)
        }
    }
}
