package com.justai.jaicf.template.scenario

data class WordModel(
    val _id: String,
    val index: Int,
    val word: String,
    val length: Int,
    var imageUrl: String,
    val next: String? = null
)