package com.example.wearablelearning

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Display
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.json.JSONArray
import org.wlcp.wlcpgameserverapi.client.WLCPGameClient
import org.wlcp.wlcpgameserverapi.dto.DisplayPhotoMessage
import org.wlcp.wlcpgameserverapi.dto.DisplayTextMessage
import org.wlcp.wlcpgameserverapi.dto.PlaySoundMessage
import org.wlcp.wlcpgameserverapi.dto.PlayVideoMessage
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

/**
 * The [GameActivity] class is launched from [ChooseTeamActivity] and is used to display the
 * relevant _State_ and _Transition_ fragments of the game.
 *
 * As a player moves through the current active game, the _State_ and _Transition_ fragments
 * switch to the next state and transition, as described by the current game information.
 */
class GameActivity : AppCompatActivity() {
    companion object {
        var map: Map<String, Any> = emptyMap()
        var states: MutableMap<String, State> = mutableMapOf()
        var transitions: MutableMap<String, Transition> = mutableMapOf()
        var currTransId: String = String()
        var gamePin: String = String()
        var prevTransIsDouble: Boolean = false
        var prevTransTypeDouble = String()
    }

    lateinit var gameInfo: GameInfo

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        /**
         * Retrieve the [GameInfo] object from the intent that started this Activity.
         *
         * The _gameInfo_ is a [GameInfo] object and is used to track user input about the game
         * (e.g., gamePin, name, etc. - See the [GameInfo] class for all relevant fields).
         */
        gameInfo = (intent.getSerializableExtra("gameInfo") as? GameInfo)!!

        /** OLD CODE

        /**
         * Retrieve the [GameInfo] object's _gamePin_ if _gameInfo_ is not _null_.
         */
        gamePin = gameInfo.gamePin.toString()

        /**
         * The fragment manager that swaps out state and transition fragments.
         */
        val fm: FragmentManager = supportFragmentManager

        mapJson()

        gameInfo?.gameName = mapGameName()

        mapStates()
        mapTransitions()
        addGamePlayerInfo(fm)

        /** If resuming a game, proceed with calling previous transition and state. */
        if(playerStartedPlaying(gameInfo)) {
            callTransition(gameInfo.currTrans!!, true, gameInfo.prevTransAnswer!!, gameInfo.prevTransType!!)
        }
        /** If a new game is started, start at the first state and transition. */
        else {
            val idx = 1

            changeState(fm, idx)
            changeTransition(fm, getOutputTransition(idx))

            LogUtils.logGamePlay("player", gameInfo, false, applicationContext)
            LogUtils.logGamePlay("gamePlay", gameInfo, false, applicationContext)
        }

        **/

        /**
         * The fragment manager that swaps out state and transition fragments.
         */
        val fm: FragmentManager = supportFragmentManager

        /** Socket */
        WLCPGameClient.getInstance().connectionOpenedCallback = WLCPGameClient.WLCPGameClientCallback {
            //Socket has been opened, now connect to the game instance
            WLCPGameClient.getInstance().connectToGameInstance()
        }

        WLCPGameClient.getInstance().connectionClosedCallback = WLCPGameClient.WLCPGameClientCallback {
            // TODO IMPLEMENT
        }

        WLCPGameClient.getInstance().connectionErrorCallback = WLCPGameClient.WLCPGameClientCallback {
            // TODO IMPLEMENT
        }

        WLCPGameClient.getInstance().connectionFailedServerHeartbeatCallback = WLCPGameClient.WLCPGameClientCallback {
            // TODO IMPLEMENT
        }

        /** Game Instance*/
        WLCPGameClient.getInstance().connectToGameInstanceCallback = WLCPGameClient.WLCPGameClientCallback {
            // TODO IMPLEMENT
        }

        WLCPGameClient.getInstance().disconnectFromGameInstanceCallback = WLCPGameClient.WLCPGameClientCallback {
            // TODO IMPLEMENT
        }

        /** State */
        WLCPGameClient.getInstance().noStateRequestCallback = WLCPGameClient.WLCPGameClientCallback {
            // TODO IMPLEMENT
        }

        WLCPGameClient.getInstance().displayTextRequestCallback = WLCPGameClient.WLCPGameClientCallbackDisplayText { msg: DisplayTextMessage ->
            // TODO IMPLEMENT - EXAMPLE BELOW
            //changeState(fm, 1, "text", msg.displayText as Object);
        }

        WLCPGameClient.getInstance().displayPhotoRequestCallback = WLCPGameClient.WLCPGameClientCallbackDisplayPhoto { msg: DisplayPhotoMessage ->
            // TODO IMPLEMENT
        }

        WLCPGameClient.getInstance().playSoundRequestCallback = WLCPGameClient.WLCPGameClientCallbackPlaySound { msg: PlaySoundMessage ->
            // TODO IMPLEMENT
        }

        WLCPGameClient.getInstance().playVideoRequestCallback = WLCPGameClient.WLCPGameClientCallbackPlayVideo { msg: PlayVideoMessage ->
            // TODO IMPLEMENT
        }

        WLCPGameClient.getInstance().displayTextDisplayPhotoRequestCallback = WLCPGameClient.WLCPGameClientCallbackDisplayTextDisplayPhoto { displayTextMessage : DisplayTextMessage, displayPhotoMessage: DisplayPhotoMessage ->
            // TODO IMPLEMENT
        }

        WLCPGameClient.getInstance().displayTextPlaySoundRequestCallback = WLCPGameClient.WLCPGameClientCallbackDisplayTextPlaySound { displayTextMessage : DisplayTextMessage, playSoundMessage: PlaySoundMessage ->
            // TODO IMPLEMENT
        }

        WLCPGameClient.getInstance().displayTextPlayVideoRequestCallback = WLCPGameClient.WLCPGameClientCallbackDisplayTextPlayVideo { displayTextMessage: DisplayTextMessage, playVideoMessage: PlayVideoMessage ->
            // TODO IMPLEMENT
        }

        WLCPGameClient.getInstance().displayPhotoPlaySoundRequestCallback = WLCPGameClient.WLCPGameClientCallbackDisplayPhotoPlaySound { displayPhotoMessage: DisplayPhotoMessage, playSoundMessage: PlaySoundMessage ->
            // TODO IMPLEMENT
        }

        /** Transition */
        WLCPGameClient.getInstance().noTransitionRequestCallback = WLCPGameClient.WLCPGameClientCallback {
            // TODO IMPLEMENT
        }

        WLCPGameClient.getInstance().singleButtonPressRequestCallback = WLCPGameClient.WLCPGameClientCallback {
            // TODO IMPLEMENT - EXAMPLE BELOW
            //changeTransition(fm, "", "button_press");
        }

        WLCPGameClient.getInstance().sequenceButtonPressRequestCallback = WLCPGameClient.WLCPGameClientCallback {
            // TODO IMPLEMENT
        }

        WLCPGameClient.getInstance().keyboardInputRequestCallback = WLCPGameClient.WLCPGameClientCallback {
            // TODO IMPLEMENT
        }

        WLCPGameClient.getInstance().randomInputRequestCallback = WLCPGameClient.WLCPGameClientCallback {
            // TODO IMPLEMENT
        }

        WLCPGameClient.getInstance().timerDurationRequestCallback = WLCPGameClient.WLCPGameClientCallbackTimerDelay { duration: Int ->
            // TODO IMPLEMENT
        }

        //Call connect (open socket connection). On success connectionOpenedCallback will be called
        WLCPGameClient.getInstance().connect(gameInfo.gamePin, gameInfo.name, Integer.valueOf(gameInfo.team!!.replace("Team ", "", false)) - 1, Integer.valueOf(gameInfo.player!!.replace("Player ", "", false)) - 1);

    }

    /**
     * The [playerStartedPlaying] function checks playerTracker.json to see if the current player has
     * already started playing the current game pin using the same team and player values. If so,
     * return true.
     * @param [gameInfo] The GameInfo object
     * @return True if gamePin, name, team, and player match a log in playerTracker.json
     */
    private fun playerStartedPlaying(gameInfo: GameInfo): Boolean {
        val fileName = "playerTracker.json"
        val file = applicationContext.getFileStreamPath(fileName)

        if(!file.exists())
            return false

        val currPlayersOnDevice = JSONArray(LogUtils.readFromFile(fileName, applicationContext))

        for(i in 0 until currPlayersOnDevice.length()) {
            val obj = currPlayersOnDevice.getJSONObject(i)

            if(obj.get("gamePin") == gameInfo.gamePin
                && (obj.get("name") == gameInfo.name || obj.get("name") == gameInfo.userName)
                && obj.get("team") == gameInfo.team!!.split(" ")[1]
                && obj.get("player") == gameInfo.player!!.split(" ")[1]) {
                    gameInfo.currState = obj.get("currState").toString()
                    gameInfo.currTrans = obj.get("currTransition").toString()

                    if(obj.has("prevTransAnswer")) {
                        gameInfo.prevTransAnswer = obj.get("prevTransAnswer").toString()
                    }

                    return true
            }
        }

        return false
    }

    /**
     * The [callTransition] function calls the output transition of the current state. Based on the
     * called transition, the next state is loaded into the state fragment as well as the transition
     * into the transition fragment.
     * @param [transId] The id associated to the game's json
     * @param [isStart] True if first transition/state for the current gameplay
     * @param [prevAnswer] The previous user answer
     * @param [prevTransType] The previous transition type
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun callTransition(transId: String, isStart: Boolean, prevAnswer: String, prevTransType: String) {
        currTransId = transId
        var stateId: Int = stateWithInputTransition(transId)

        if(isStart) {
            stateId = stateWithOutputTransition(transId)
        }

        val fm: FragmentManager = supportFragmentManager

        changeState(fm, stateId)
        val outputTransition = getOutputTransition(stateId)

        if(prevTransIsDouble) {
            gameInfo.prevTransType = prevTransTypeDouble
        }
        else {
            gameInfo.prevTransType = prevTransType
        }

        /** If there is only one output transition. */
        if(!outputTransition.contains(",") or isAllSameType(outputTransition)) {
            changeHelperTransition(fm, "none")
            changeTransition(fm, outputTransition)

            prevTransIsDouble = false
        }
        /** If there are multiple output transitions with different types
         * (e.g. timer and button press). */
        else {
            val transList = outputTransition.replace(" ", "").split(',')

            if(transList.size == 2) {
                val transType0 = transitions[transList[0]]?.type.toString()
                val transType1 = transitions[transList[1]]?.type.toString()
                val timerStr: String

                /** If Timer is the first transition. */
                if(transType0.contains("timer")) {
                    changeHelperTransition(fm, transList[0])
                    changeTransition(fm, transList[1])

                    timerStr = transitions[transList[0]]?.content.toString().plus("s timer")
                    prevTransTypeDouble = "$timerStr and $transType1"
                }
                /** If Timer is the second transition. */
                else if(transType1.contains("timer")){
                    changeHelperTransition(fm, transList[1])
                    changeTransition(fm, transList[0])

                    timerStr = transitions[transList[1]]?.content.toString().plus("s timer")
                    prevTransTypeDouble = "$timerStr and $transType0"
                }

                prevTransIsDouble = true
            }
        }

        gameInfo.prevTransAnswer = prevAnswer
        gameInfo.currTransAnswer = String()
        LogUtils.logGamePlay("player", gameInfo, false, applicationContext)
        LogUtils.logGamePlay("gamePlay", gameInfo, false, applicationContext)
    }


    /**
     * The [mapJson] utility function retrieves the JSON file associated with a given game pin
     * from a [GameInfo] object.
     */
    private fun mapJson() {
        /**
         * Retrieve the contents of a JSON file and store in _jsonContents_.
         */
        val jsonContents: String = resources.openRawResource(getGameFileIdentifier(gamePin))
            .bufferedReader().use { it.readText() }

        map = StringUtils.parseJsonWithGson(jsonContents)
    }

    /**
     * The [mapGameName] utility function retrieves the name of the current game from map.
     * @return name of game
     */
    private fun mapGameName(): String {
        val gameInfo: String = map.getValue("game").toString()

        return gameInfo.substringAfter("game_name=").substringBefore(", team_cnt=")
    }

    /**
     * The [mapStates] utility function retrieves the data for each state (i.e. name, players, text,
     * photo, sound, video, trans_inputs, trans_outputs) from _map_ and stores them in _states_.
     */
    private fun mapStates() {
        val stateList: ArrayList<Any> = map.getValue("states") as ArrayList<Any>

        for(s in stateList) {
            val stateStr: String = s.toString()
            val stateId: String = stateStr.substringAfter("{id=").substringBefore(", name=")

            val newState = State(
                stateId,
                stateStr.substringAfter(", name=").substringBefore(", players="),
                stateStr.substringAfter(", players=").substringBefore(", text="),
                stateStr.substringAfter(", text=").substringBefore(", photo="),
                stateStr.substringAfter(", photo=").substringBefore(", sound="),
                stateStr.substringAfter(", sound=").substringBefore(", video="),
                stateStr.substringAfter(", video=").substringBefore(", trans_inputs="),
                stateStr.substringAfter(", trans_inputs=").substringBefore(", trans_outputs="),
                stateStr.substringAfter(", trans_outputs=").substringBefore("}")
            )

            states[stateId] = newState
        }
    }

    /**
     * The [mapTransitions] utility function retrieves the data for each transition (i.e. id, type,
     * content) from _map_ and stores the in _transitions_.
     */
    private fun mapTransitions() {
        val transList: ArrayList<Any> = map.getValue("transitions") as ArrayList<Any>

        for(t in transList) {
            val transStr: String = t.toString()
            val transId: String = transStr.substringAfter("{id=").substringBefore(", type=")

            val newTransition = Transition(
                transId,
                transStr.substringAfter(", type=").substringBefore(", content="),
                transStr.substringAfter(", content=").substringBefore("}")
            )

            transitions[transId] = newTransition
        }
    }

    /**
     * The [getOutputTransition] utility function returns a string of output transition ids.
     * @param [idx] The index of state.
     * @return The string of output transitions.
     */
    private fun getOutputTransition(idx: Int): String {
        val outputs: List<String> = states["state_$idx"]!!.trans_outputs

        if(outputs.size == 1) {
            return outputs[0]
        } else {
           return outputs.joinToString()
        }
    }

    /**
     * The [isAllSameType] utility function determines if all output transitions are of the same
     * type (e.g. all button press).
     * @param [transStr] The string of output transition ids.
     * @return True if all output transitions are of the same type.
     */
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

    /**
     * The [stateWithInputTransition] utility function returns the state id with the given input
     * transition.
     * @param [transId] The id of the input transition.
     * @return The id of the state.
     */
    private fun stateWithInputTransition(transId: String): Int {
        val idx = 0

        for(i in 1..states.size) {
            if(states["state_$i"]?.trans_inputs?.contains(transId) == true) {
                return i
            }
        }

        return idx
    }

    /**
     * The [stateWithOutputTransition] utility function returns the state id with the given output
     * transition.
     * @param [transId] The id of the output transition.
     * @return The id of the state.
     */
    private fun stateWithOutputTransition(transId: String): Int {
        val idx = 0

        for(i in 1..states.size) {
            if(states["state_$i"]?.trans_outputs?.contains(transId) == true) {
                return i
            }
        }

        return idx
    }

    /**
     * The [addGamePlayerInfo] function adds the [GamePlayerInfoFragment] with name, player, and team
     * data to the top of the Activity.
     * @param [fm] The fragment manager.
     */
    private fun addGamePlayerInfo(fm: FragmentManager) {
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.replace(R.id.frameLayout0, GamePlayerInfoFragment())
        ft.commit()
    }

    /**
     * The [changeState] utility function uses the fragment manager to swap the current state with
     * the next state. Function checks the type of the state and appropriately replaces the
     * current state fragment with a new state fragment.
     * @param [fm] The fragment manager.
     * @param [idx] The idx/id of the state.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun changeState(fm: FragmentManager, idx: Int) {
        val ft: FragmentTransaction = fm.beginTransaction()
        val bundle = Bundle()

        val type = states["state_$idx"]?.let { determineType(it) }

        gameInfo.currState = "state_$idx"
        gameInfo.currStateStartTime = getTimeStamp()

        /** Text state fragment. */
        if(type == "text") {
            val text = states["state_$idx"]?.text.toString()

            val fragInfo = StateTextFragment()
            bundle.putString("text", text)
            fragInfo.arguments = bundle
            ft.replace(R.id.frameLayout1, fragInfo)
            ft.commit()
        }
        /** Photo state fragment. */
        else if(type == "photo") {
            val text = String()
            val image = states["state_$idx"]?.photo.toString()

            val fragInfo = StatePhotoFragment()
            bundle.putString("text", text)
            bundle.putString("image", image)
            fragInfo.arguments = bundle
            ft.replace(R.id.frameLayout1, fragInfo)
            ft.commit()
        }
        /** Sound state fragment. */
        else if(type == "sound") {
            val text = String()
            val sound = states["state_$idx"]?.sound.toString()

            val fragInfo = StateSoundFragment()
            bundle.putString("text", text)
            bundle.putString("sound", sound)
            fragInfo.arguments = bundle
            ft.replace(R.id.frameLayout1, fragInfo)
            ft.commit()
        }
        /** Text & Photo state fragment. */
        else if(type == "text&photo") {
            val text = states["state_$idx"]?.text.toString()
            val image = states["state_$idx"]?.photo.toString()

            val fragInfo = StatePhotoFragment()
            bundle.putString("text", text)
            bundle.putString("image", image)
            fragInfo.arguments = bundle
            ft.replace(R.id.frameLayout1, fragInfo)
            ft.commit()
        }
        /** Text & Sound state fragment. */
        else if(type == "text&sound") {
            val text = states["state_$idx"]?.text.toString()
            val sound = states["state_$idx"]?.sound.toString()

            val fragInfo = StateSoundFragment()
            bundle.putString("text", text)
            bundle.putString("sound", sound)
            fragInfo.arguments = bundle
            ft.replace(R.id.frameLayout1, fragInfo)
            ft.commit()
        }
        /** Text & Video state fragment. */
        else if(type == "text&video") {
            val text = states["state_$idx"]?.text.toString()
            val video = states["state_$idx"]?.video.toString()

            val fragInfo = StateVideoFragment()
            bundle.putString("text", text)
            bundle.putString("video", video)
            fragInfo.arguments = bundle
            ft.replace(R.id.frameLayout1, fragInfo)
            ft.commit()
        }
        /** Photo & Sound state fragment. */
        else if(type == "photo&sound") {
            val photo = states["state_$idx"]?.photo.toString()
            val sound = states["state_$idx"]?.sound.toString()

            val fragInfo = StatePhotoAndSoundFragment()
            bundle.putString("photo", photo)
            bundle.putString("sound", sound)
            fragInfo.arguments = bundle
            ft.replace(R.id.frameLayout1, fragInfo)
            ft.commit()
        }
    }

    /**
     * The [determineType] utility function determines the type of state fragment to be called by
     * checking the text, photo, sound, and video values for the state.
     * @param [state] The State object.
     * @return Keyword that represents the state fragment to be called.
     */
    private fun determineType(state: State): String {
        var isText = false
        var isPhoto = false
        var isSound = false
        var isVideo = false

        if(!StringUtils.isEmptyOrBlank(state.text)) {
            isText = true
        }

        if(!StringUtils.isEmptyOrBlank(state.photo)) {
            isPhoto = true
        }

        if(!StringUtils.isEmptyOrBlank(state.sound)) {
            isSound = true
        }

        if(!StringUtils.isEmptyOrBlank(state.video)) {
            isVideo = true
        }

        /** Return a keyword based on the concatenation of true booleans. */
        if(isSound && isPhoto) {
            return "photo&sound"
        }
        else if(isText && isSound) {
            return "text&sound"
        }
        else if(isText && isVideo) {
            return "text&video"
        }
        else if(isText && isPhoto) {
            return "text&photo"
        }
        else if(isSound) {
            return "sound"
        }
        else if(isVideo) {
            return "video"
        }
        else if(isPhoto) {
            return "photo"
        }
        else if(isText) {
            return "text"
        }
        else {
            return String()
        }
    }

    /**
     * The [changeHelperTransition] utility function determines if there needs to be two transition
     * fragments (the second being the helper). For example, if there is a timer and button press
     * transition, the helper transition would be the timer and thus this fragment would be replaced
     * with [TransitionTimerFragment]. Otherwise, the helper is replaced with [TransitionBlankFragment].
     * @param [fm] The fragment manager.
     * @param [helperTransition] The helper transitions's id.
     */
    private fun changeHelperTransition(fm: FragmentManager, helperTransition: String) {
        val type = transitions[helperTransition]?.type.toString()
        val id = transitions[helperTransition]?.id.toString()
        val content = transitions[helperTransition]?.content.toString()

        val ft: FragmentTransaction = fm.beginTransaction()
        val bundle = Bundle()
        bundle.putString("id", id)
        bundle.putString("content", content)
        bundle.putString("frameLayout", "frameLayout2a")

        if(helperTransition == "none") {
            val fragInfo = TransitionBlankFragment()
            fragInfo.arguments = bundle
            ft.replace(R.id.frameLayout2a, fragInfo)
            ft.commit()
            return
        }
        else if(type.contains("timer")) {
            val fragInfo = TransitionTimerFragment()
            fragInfo.arguments = bundle
            ft.replace(R.id.frameLayout2a, fragInfo)
            ft.commit()
        }
    }

    /**
     * The [changeTransition] utility function changes the current transition by replacing the
     * transition fragment with the appropriate transition fragment associated to the next
     * transition.
     * @param [fm] The fragment manager.
     * @param [transition] The transitions's id.
     */
    private fun changeTransition(fm: FragmentManager, transition: String) {
        var type = transitions[transition]?.type.toString()
        var id = transitions[transition]?.id.toString()
        var content = transitions[transition]?.content.toString()

        /** If there are multiple transitions retrieve list. */
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

        gameInfo.currTrans = id

        val ft: FragmentTransaction = fm.beginTransaction()
        val bundle = Bundle()
        bundle.putString("id", id)
        bundle.putString("content", content)
        bundle.putString("frameLayout", "frameLayout2")

        /** Button Press transition fragment. */
        if(type.contains("button_press")) {
            val fragInfo = TransitionBtnPressFragment()
            fragInfo.arguments = bundle
            ft.replace(R.id.frameLayout2, fragInfo)
            ft.commit()
        }
        /** Color Sequence transition fragment. */
        else if(type.contains("color_sequence")) {
            val fragInfo = TransitionSequenceFragment()
            fragInfo.arguments = bundle
            ft.replace(R.id.frameLayout2, fragInfo)
            ft.commit()
        }
        /** Text Entry transition fragment. */
        else if(type.contains("text_entry")) {
            val fragInfo = TransitionTextEntryFragment()
            fragInfo.arguments = bundle
            ft.replace(R.id.frameLayout2, fragInfo)
            ft.commit()
        }
        /** Random transition fragment. */
        else if(type.contains("random")) {
            val fragInfo = TransitionRandomFragment()
            fragInfo.arguments = bundle
            ft.replace(R.id.frameLayout2, fragInfo)
            ft.commit()
        }
        /** Timer transition fragment. */
        else if(type.contains("timer")) {
            val fragInfo = TransitionTimerFragment()
            fragInfo.arguments = bundle
            ft.replace(R.id.frameLayout2, fragInfo)
            ft.commit()
        }
        /** End Game transition fragment. */
        else {
            val fragInfo = TransitionEndGameFragment()
            ft.replace(R.id.frameLayout2, fragInfo)
            ft.commit()
        }
    }


    /**
     * This [onBackPressed] override builds the dialog for the _back_ button on [GameActivity]
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBackPressed() {
        MaterialAlertDialogBuilder(this, R.style.Theme_WearableLearning_AlertDialog)
            .setMessage(resources.getString(R.string.confirm_back_text))
            .setNegativeButton(resources.getString(R.string.no_text)) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(resources.getString(R.string.yes_text)) { _, _ ->
                gameInfo.interactionType = "backButton"
                gameInfo.currTransAnswer = "backButton"

                /** Build _intent_ to switch from [GameActivity] to [MainActivity]. */
                val intent = Intent(this@GameActivity, MainActivity::class.java)

                LogUtils.logGamePlay("player", gameInfo, true, applicationContext)
                LogUtils.logGamePlay("gamePlay", gameInfo, true, applicationContext)

                /**
                 * Note -- FLAG_ACTIVITY_CLEAR_TOP:
                 * If set, and the activity being launched is already running in the current task,
                 * then instead of launching a new instance of that activity, all of the other
                 * activities on top of it will be closed and this Intent will be delivered to the
                 * (now on top) old activity as a new Intent.
                 */
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                /** Add the [GameInfo] objects into _intent_ */
                intent.putExtra("gameInfo", gameInfo)

                /** Launch [MainActivity] */
                startActivity(intent)
            }
            .show()
    }


    /**
     * The [getGameFileIdentifier] utility function returns the integer identifier for the game
     * json in the raw resources folder.
     * @param [gamePin] The _gamePin_ value stored in a [GameInfo] object
     * @return The Int value of the json
     */
    private fun getGameFileIdentifier(gamePin: String): Int {
        val fileName = "game$gamePin"

        return resources.getIdentifier(fileName, "raw", applicationContext?.packageName)
    }

    /**
     * Returns the current timestamp.
     * @return string timestamp
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTimeStamp(): String {
        val currTime =
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.N){
                DateTimeFormatter.ISO_INSTANT.format(Instant.now())
            } else {
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                simpleDateFormat.format(Date())
            }

        return currTime.toString()
    }
}
