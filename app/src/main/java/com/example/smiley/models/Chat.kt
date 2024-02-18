package com.example.smiley.models

data class Chat(
    var id: String = "",
    var uid: String = "",
    var type: String = "",
    var name: String = "",
    var message: String = "",
    var time: String = "",
    var read: Boolean = false,
)
