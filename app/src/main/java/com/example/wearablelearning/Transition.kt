package com.example.wearablelearning

/**
 * The [Transition] class represents one transition object, which is populated when a game's json is
 * read and parsed in [GameActivity]. Each transition has an id, type, and content (where content
 * represents the response that the user is expected to complete in order to move on.
 */
class Transition(_id: String, _type: String, _content: String) {
    var id: String = _id
    var type: String = _type
    var content: String = _content
}
