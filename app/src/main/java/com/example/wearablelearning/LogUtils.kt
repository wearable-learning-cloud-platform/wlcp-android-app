package com.example.wearablelearning

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.time.Instant
import java.time.format.DateTimeFormatter

object LogUtils {
    //TODO
    //log more events (rn only on correct submit and back)
    //clear jsons when exit game, finish game, after so many hrs of inactivity
    //any way to make jsons more readable?
    //whenever writing to file also write to logcat (info, use name of file as keyword)

    fun logGamePlay(fileType: String, data: GameInfo, isAppExit: Boolean, context: Context) {
        var file: String = getFileName(fileType)

        val jsonObj = JSONObject()

        jsonObj.put("name", getName(data.name, data.userName))
        jsonObj.put("guest", getGuest(data.name, data.userName))
        jsonObj.put("gamePin", data.gamePin)
        jsonObj.put("team", getNumber(data.team))
        jsonObj.put("player", getNumber(data.player))
        jsonObj.put("currState", data.currState)
        jsonObj.put("currTransition", data.currTrans)
        jsonObj.put("timeEnterState", getStateTimeStamp())
        jsonObj.put("timeExitApp", getExitAppTimeStamp(isAppExit))
        jsonObj.put("prevTransAnswer", data.prevTransAnswer)

        val fileOutputStream: FileOutputStream

        try {
            //if file does not exist, create file with new log
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

    private fun getFileName(str: String): String {
        if(str == "player") {
            return "playerTracker.json"
        }
        else if (str == "gamePlay") {
            return "gamePlayLog.json"
        }

        return String()
    }

    private fun fileExists(fileName: String, context: Context): Boolean {
        var file = context.getFileStreamPath(fileName)
        return file.exists()
    }

    private fun getName(name: String?, userName: String?): String? {
        if(name.isNullOrEmpty()) {
            return userName
        }

        return name
    }

    private fun getGuest(name: String?, userName: String?): Boolean {
        if(name.isNullOrEmpty()) {
            return false
        }

        return true
    }

    private fun getNumber(str: String?): String {
        if (str != null) {
            return str.split(" ")[1]
        }

        return str.toString()
    }

    private fun getStateTimeStamp(): String {
        return DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
    }

    private fun getExitAppTimeStamp(isAppExit: Boolean): String {
        if(isAppExit) {
            return DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
        }

        return String()
    }
}