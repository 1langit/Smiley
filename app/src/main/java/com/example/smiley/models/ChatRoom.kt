package com.example.smiley.models

data class ChatRoom(
    var id: String = "",
    var participants: List<String> = arrayListOf(),
    var lastMessage: String = "",
    var time: String = ""
)
