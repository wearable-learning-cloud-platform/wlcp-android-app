package com.example.wearablelearning

import android.content.res.Resources

/**
 * String utility object.
 * Provides functions for validating and altering strings.
 */
object StringUtils {
    /**
     * Checks if a string is empty or blank (no characters).
     * @param str: String
     * @return true if string is empty or blank
     */
    @JvmStatic
    fun isEmptyOrBlank(str: String): Boolean {
        if(str.trim().isNullOrEmpty() or str.trim().isBlank()) {
            return true
        }

        return false
    }

    /**
     * Checks if a string is in a list of strings.
     * @param str: String
     * @param listOfStrs: List<String>
     * @return true if string is in list of strings
     */
    @JvmStatic
    fun isStringInList(str: String, listOfStrs: List<String>): Boolean {
        if(str !in listOfStrs) {
            return false
        }

        return true
    }

    /**
     * Given a res/raw json file and a json key, retrieves the values for that key as a list of strings.
     * @param res: resources
     * @param fileId: id of json file in res/raw (ex: R.raw.data)
     * @param key: key in json that outputted pair with
     * @return list of string values
     */
    @JvmStatic
    fun getValuesFromJSON(res: Resources, fileId: Int, key: String): List<String> {
        /**
         * The contents of a json file converted into a string.
         */
        val jsonContents: String = res.openRawResource(fileId)
            .bufferedReader().use { it.readText() }
            .filter { !it.isWhitespace() }

        return extractValuesFromJSONString(jsonContents, key, ',')
    }

    /**
     * Given a json in string format, extracts the values that pair with a key.
     * @param str: json as string
     * @param key: key to retrieve values for
     * @param delimiter: char to split string on
     * @return list of strings
     */
    @JvmStatic
    fun extractValuesFromJSONString(str: String, key: String, delimiter: Char): List<String> {
        /**
         * String to flag the beginning of substring.
         */
        val startSubstr: String = key.plus("\":[")

        /**
         * Character to flag the end of substring.
         */
        val endSubstr = ']'

        /**
         * Substring of str that lies between startSubstr and endSubstr.
         */
        val substr: String = str.substringAfter(startSubstr).substringBefore(endSubstr)

        if(isEmptyOrBlank(startSubstr) or isEmptyOrBlank(substr)) {
            return emptyList()
        }

        return substr.split(delimiter)
    }
}