package com.example.smiley.models

data class Chat(
    var id: String = "",
    var senderUid: String = "",
    var recieverUid: String = "",
    var message: String = "",
    var time: String = ""
)
