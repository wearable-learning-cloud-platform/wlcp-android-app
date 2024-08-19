package com.example.wearablelearning

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import org.wlcp.wlcpgameserverapi.client.WLCPGameClient

class TransitionRandomFragment : Fragment() {
    private lateinit var wlcpGameClient: WLCPGameClient

    companion object {
        fun newInstance(): TransitionRandomFragment {
            return TransitionRandomFragment()
        }
    }

    fun setWLCPGameClient(wlcpGameClient: WLCPGameClient) {
        this.wlcpGameClient = wlcpGameClient
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transition_random, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** The _id_ is the current transition's id. */
        var id = this.requireArguments().getString("id")

        val continueButton = view.findViewById<Button>(R.id.continue_btn)

        continueButton.setOnClickListener {
            // Send the random input to the backend API
            wlcpGameClient.sendRandomInput()
        }
    }
}
