package com.example.wearablelearning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * Game activity.
 */
class GameActivity : AppCompatActivity() {
    companion object {
        var map: Map<String, Any> = emptyMap()
        var states: MutableMap<String, State> = mutableMapOf()
        var transitions: MutableMap<String, Transition> = mutableMapOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val gameInfo = intent.getSerializableExtra("gameInfo") as? GameInfo

        var fm: FragmentManager = supportFragmentManager

        mapJson()
        mapStates()
        mapTransitions()

        var idx = 1
        changeState(fm, idx)
        changeTransition(fm, getOutputTransition(idx))
    }

    fun callTransition(transId: String) {
        val nextStateId: Int = stateWithInputTransition(transId)
        var fm: FragmentManager = supportFragmentManager

        changeState(fm, nextStateId)
        val outputTransition = getOutputTransition(nextStateId)

        // if there is only one output transition
        if(!outputTransition.contains(",") or isAllSameType(outputTransition)) {
            changeTransition(fm, outputTransition)
        }

    }

    private fun mapJson() {
        val jsonContents: String = resources.openRawResource(R.raw.game12) //TODO
            .bufferedReader().use { it.readText() }

        map = StringUtils.parseJsonWithGson(jsonContents)
    }

    private fun mapStates() {
        val stateList: ArrayList<Any> = map.getValue("states") as ArrayList<Any>

        for(s in stateList) {
            var stateStr: String = s.toString()
            var stateId: String = stateStr.substringAfter("{id=").substringBefore(", name=")

            val newState = State(
                stateId,
                stateStr.substringAfter(", name=").substringBefore(", players="),
                stateStr.substringAfter(", players=").substringBefore(", type="),
                stateStr.substringAfter(", type=").substringBefore(", content="),
                stateStr.substringAfter(", content=").substringBefore(", trans_inputs="),
                stateStr.substringAfter(", trans_inputs=").substringBefore(", trans_outputs="),
                stateStr.substringAfter(", trans_outputs=").substringBefore("}")
            )

            states[stateId] = newState
        }
    }

    private fun mapTransitions() {
        val transList: ArrayList<Any> = map.getValue("transitions") as ArrayList<Any>

        for(t in transList) {
            var transStr: String = t.toString()
            var transId: String = transStr.substringAfter("{id=").substringBefore(", type=")

            val newTransition = Transition(
                transId,
                transStr.substringAfter(", type=").substringBefore(", content="),
                transStr.substringAfter(", content=").substringBefore("}")
            )

            transitions[transId] = newTransition
        }
    }

    private fun getOutputTransition(idx: Int): String {
        var outputs: List<String> = states["state_$idx"]!!.trans_outputs

        if(outputs.size == 1) {
            return outputs[0]
        } else {
           return outputs.joinToString()
        }
    }

    private fun isAllSameType(transStr: String): Boolean {
        val transList = transStr.replace(" ", "").split(',')
        val transType = transitions[transList[0]]?.type.toString()

        for(t in transList) {
            if(transitions[t]?.type.toString() != transType) {
                return false
            }
        }

        return true
    }

    private fun stateWithInputTransition(transId: String): Int {
        var idx = 0

        for(i in 1..states.size) {
            if(states["state_$i"]?.trans_inputs?.contains(transId) == true) {
                return i
            }
        }

        return idx
    }

    private fun changeState(fm: FragmentManager, idx: Int) {
        val ft: FragmentTransaction = fm.beginTransaction()
        val bundle = Bundle()
        val fragInfo = StateFragment()

        val content = states["state_$idx"]?.content.toString()
        bundle.putString("content", content)
        fragInfo.arguments = bundle
        ft.replace(R.id.frameLayout1, fragInfo)
        ft.commit()
    }

    private fun changeTransition(fm: FragmentManager, transition: String) {
        var type = transitions[transition]?.type.toString()
        var id = transitions[transition]?.id.toString()
        var content = transitions[transition]?.content.toString()

        //if multiple transitions
        if(transition.contains(",")) {
            val transList = transition.replace(" ", "").split(",")
            type = transitions[transList[0]]?.type.toString()
            id = ""
            content = ""

            for(t in transList) {
                if(StringUtils.isEmptyOrBlank(content)) {
                    content = transitions[t]?.content.toString()
                    id = transitions[t]?.id.toString()
                }
                else {
                    content = content + ";;" + transitions[t]?.content.toString()
                    id = id + ";;" + transitions[t]?.id.toString()
                }
            }
        }

        val ft: FragmentTransaction = fm.beginTransaction()
        val bundle = Bundle()
        bundle.putString("id", id)
        bundle.putString("content", content)

        if(type.contains("button_press")) {
            val fragInfo = TransitionBtnPressFragment()
            fragInfo.arguments = bundle
            ft.replace(R.id.frameLayout2, fragInfo)
            ft.commit()
        }
        else if(type.contains("color_sequence")) {
            val fragInfo = TransitionSequenceFragment()
            fragInfo.arguments = bundle
            ft.replace(R.id.frameLayout2, fragInfo)
            ft.commit()
        }
        else if(type.contains("text_entry")) {
            val fragInfo = TransitionTextEntryFragment()
            fragInfo.arguments = bundle
            ft.replace(R.id.frameLayout2, fragInfo)
            ft.commit()
        }
        else {
            val fragInfo = TransitionEndGameFragment()
            ft.replace(R.id.frameLayout2, fragInfo)
            ft.commit()
        }
    }
}
