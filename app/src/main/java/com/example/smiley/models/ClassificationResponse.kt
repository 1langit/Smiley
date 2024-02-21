package com.example.smiley.models

data class ClassificationResponse(
    val classification: String,
    val confident_score: Double,
    val elaboration: String,
    val image_path: String
)