package com.example.wearablelearning

class State(_id: String, _name: String, _players: String, _text: String, _photo: String, _sound: String, _video: String, _trans_inputs: String, _trans_outputs: String) {
    var id: String = _id
    var name: String = _name
    var players: String = _players
    var text: String = _text
    var photo: String = _photo
    var sound: String = _sound
    var video: String = _video
    var trans_inputs: List<String> = convertStringToList(_trans_inputs)
    var trans_outputs: List<String> = convertStringToList(_trans_outputs)

    private fun convertStringToList(str: String): List<String> {
        return str.split(", ")
    }
}
