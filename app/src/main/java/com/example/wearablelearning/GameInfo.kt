package com.example.wearablelearning

import java.io.Serializable

/**
 * The [GameInfo] class is used to track user input and information as the user moves from one
 * app Activity to another.
 */
class GameInfo() : Serializable {
    var gamePin: String? = null
    var gameName: String? = null
    var name: String? = null
    var userName: String? = null
    var team: String? = null
    var player: String? = null
    var currState: String? = null
    var currTrans: String? = null
    //timeEnterState
    //timeExitApp
    //prevTransAnswer: if currState is 2 then this should be answer to state 1's question
}
