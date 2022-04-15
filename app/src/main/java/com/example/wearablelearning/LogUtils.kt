package com.example.wearablelearning

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.time.Instant
import java.time.format.DateTimeFormatter

/**
 * Log utility object.
 * Provides functions for logging interactions to internal storage jsons.
 */
object LogUtils {

    /**
     * Writes log data to an internal json file, where log data is mostly made up GameInfo data.
     * @param fileType: String
     * @param data: GameInfo
     * @param isAppExit: Boolean
     * @param context: Context
     */
    fun logGamePlay(fileType: String, data: GameInfo, isAppExit: Boolean, context: Context) {
        /**
         * The file name based on the fileType.
         */
        var file: String = getFileName(fileType)

        /**
         * The json object where log data is put into.
         */
        val jsonObj = JSONObject()

        jsonObj.put("name", getName(data.name, data.userName))
        jsonObj.put("guest", getGuest(data.name))
        jsonObj.put("gamePin", data.gamePin)
        jsonObj.put("team", getNumber(data.team))
        jsonObj.put("player", getNumber(data.player))
        jsonObj.put("currState", data.currState)
        jsonObj.put("currTransition", data.currTrans)
        jsonObj.put("timeEnterState", getStateTimeStamp())
        jsonObj.put("timeExitApp", getExitAppTimeStamp(isAppExit))
        jsonObj.put("prevTransAnswer", data.prevTransAnswer)

        /**
         * The file output stream for writing to the json file.
         */
        val fileOutputStream: FileOutputStream

        try {
            //if file does not currently exist, create file with new log
            if(!fileExists(file, context)) {
                val jsonArray = JSONArray()
                jsonArray.put(jsonObj)

                fileOutputStream = context.openFileOutput(file, AppCompatActivity.MODE_PRIVATE)
                fileOutputStream.write("$jsonArray\n".toByteArray())
                fileOutputStream.close()
            }
            //if file exists
            else {
                val jsonArray = JSONArray(readFromFile(file, context))

                //if file is playerTracker, either replace old log if exists or append new log
                if(file == "playerTracker.json") {
                    for(i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)

                        if(obj.get("gamePin") == jsonObj.get("gamePin")
                            && obj.get("name") == jsonObj.get("name")
                            && obj.get("team") == jsonObj.get("team")
                            && obj.get("player") == jsonObj.get("player")) {
                                //log exists for player so remove old log and add new log
                                jsonArray.remove(i)
                                jsonArray.put(jsonObj)

                                fileOutputStream = context.openFileOutput(file, AppCompatActivity.MODE_PRIVATE)
                                fileOutputStream.write("$jsonArray\n".toByteArray())
                                fileOutputStream.close()

                                return
                        }
                    }

                    //log does not exists for player so append new log
                    jsonArray.put(jsonObj)

                    fileOutputStream = context.openFileOutput(file, AppCompatActivity.MODE_PRIVATE)
                    fileOutputStream.write("$jsonArray\n".toByteArray())
                    fileOutputStream.close()
                }
                //if file is not playerTracker or playerTrack does not have current player, append new log
                else {
                    jsonArray.put(jsonObj)

                    fileOutputStream = context.openFileOutput(file, AppCompatActivity.MODE_PRIVATE)
                    fileOutputStream.write("$jsonArray\n".toByteArray())
                    fileOutputStream.close()
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    /**
     * Reads from a json line by line and returns the contents as a string.
     * @param fileName: String
     * @param context: Context
     * @return string holding file contents
     */
    fun readFromFile(fileName: String, context: Context): String {
        var inputStream: InputStream? = context.openFileInput(fileName)
        var fileAsString = ""

        if(inputStream != null) {
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            var receiveString: String? = ""
            val stringBuilder = StringBuilder()

            while (bufferedReader.readLine().also { receiveString = it } != null) {
                stringBuilder.append(receiveString)
            }

            fileAsString = stringBuilder.toString()
        }

        inputStream?.close()

        return fileAsString
    }

    /**
     * Return the name of the json file based on a keyword.
     * @param str: String
     * @return string json file name
     */
    private fun getFileName(str: String): String {
        if(str == "player") {
            return "playerTracker.json"
        }
        else if (str == "gamePlay") {
            return "gamePlayLog.json"
        }

        return String()
    }

    /**
     * Checks if a file exists in internal storage.
     * @param fileName: String
     * @param context: Context
     * @return true if file exists
     */
    private fun fileExists(fileName: String, context: Context): Boolean {
        var file = context.getFileStreamPath(fileName)
        return file.exists()
    }

    /**
     * Gets the identifiable name of the player, whether that is name or username.
     * @param name: String?
     * @param userName: String?
     * @return string username if it exists, otherwise returns string name
     */
    private fun getName(name: String?, userName: String?): String? {
        if(name.isNullOrEmpty()) {
            return userName
        }

        return name
    }

    /**
     * Returns true if the name of the player exists.
     * @param name: String?
     * @param userName: String?
     * @return true if name exists, otherwise false
     */
    private fun getGuest(name: String?): Boolean {
        if(name.isNullOrEmpty()) {
            return false
        }

        return true
    }

    /**
     * Returns only the trailing number of a string after a space.
     * @param str: String
     * @return string number
     */
    private fun getNumber(str: String?): String {
        if (str != null) {
            return str.split(" ")[1]
        }

        return str.toString()
    }

    /**
     * Returns the current timestamp.
     * @return string timestamp
     */
    private fun getStateTimeStamp(): String {
        return DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
    }

    /**
     * Returns the current timestamp only if isAppExit is true.
     * @return string timestamp
     */
    private fun getExitAppTimeStamp(isAppExit: Boolean): String {
        if(isAppExit) {
            return DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        }

        return String()
    }
}