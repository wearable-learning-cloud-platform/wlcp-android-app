package com.example.wearablelearning

import java.io.Serializable

/**
 * The [GameInfo] class is used to track user input and information as the user moves from one
 * app Activity to another.
 */
class GameInfo() : Serializable {
    /**
     * Value inputted by player in MainActivity.
     */
    var gamePin: String? = null

    /**
     * Name of game associated to gamePin.
     */
    var gameName: String? = null

    /**
     * The player's name if the player inputted a name (i.e. logged in as guest) during LoginActivity.
     * If the player is not a guest, this variable remains null and userName is not null.
     */
    var name: String? = null

    /**
     * The player's username if the player inputted a username (i.e. logged in with account) during
     * LoginActivity. If the player is a guest, this variable remains null and name is not null.
     */
    var userName: String? = null

    /**
     * The team number selected by the player in ChooseTeamActivity.
     */
    var team: String? = null

    /**
     * The player number selected by the player in ChooseTeamActivity.
     */
    var player: String? = null

    /**
     * The transition currently visible to the player.
     */
    var currTrans: String? = null

    /**
     * The state currently visible to the player.
     */
    var currState: String? = null

    /**
     * The timestamp of when the current state was entered. If multiple interactions happen within
     * one state, all logs for that one state will have the same currStateStartTime.
     */
    var currStateStartTime: String? = null

    /**
     * The type of the previous transition. For example, if the current state is state 2 then
     * prevTransType is the type of state 1 (assuming linear game: start -> state 1 -> state 2).
     * For cases where there are multiple transitions (e.g. timer and button press), both transitions
     * are concatenated here (e.g. "3s timer and button_press").
     */
    var prevTransType: String? = null

    /**
     * The answer to the previous transition. For example, if the current state is state 2 then
     * prevTransAnswer is the answer to state 1 (assuming linear game: start -> state 1 -> state 2).
     * For cases where timer is the only previous transitions, the prevTransAnswer is the duration of
     * the timer since the player does not actually input an answer (e.g. "3s timer"). For cases
     * where there are more than one transition, the answer that caused the game to progress is the
     * prevTransAnswer (e.g. there is a 3s timer and RED button press and the player did not press the
     * correct button in time then prevTransAnswer = "3s timer") (e.g. there is a 3s timer and RED
     * button press and the player pressed the correct button before the timer expired then
     * prevTransAnswer = "RD").
     */
    var prevTransAnswer: String? = null

    /**
     * The input to the current transition at the time of a logged interaction. There are two cases:
     * 1) the player clears the response (currTransAnswer = answer right before clearing, and
     * 2) the player inputs a wrong answer and the game does not have a wrong answer transition
     */
    var currTransAnswer: String? = null

    /**
     * The interaction that triggered logging: clearButton, submitButton, backButton, colorButtonPress,
     * or timer expired. For cases where there are more than one transition, the interaction that
     * caused the game to progress is the interactionType (e.g. there is a 3s timer and RED button
     * press and the player did not press the correct button in time then interactionType = "3s timer
     * expired") (e.g. there is a 3s timer and RED button press and the player pressed the correct
     * button before the timer expired then interactionType = "coloredButtonPress").
     */
    var interactionType: String? = null
}
