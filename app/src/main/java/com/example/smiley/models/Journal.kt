package com.example.smiley.models

data class Journal(
    var id: String = "",
    var uid: String = "",
    var date: String = "",
    var brushTeeth: Boolean = false,
    var noDentalProblem: Boolean = false,
    var lessSugar: Boolean = false,
    var checkup: Boolean = false
)
