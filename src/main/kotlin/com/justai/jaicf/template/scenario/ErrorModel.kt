package com.justai.jaicf.template.scenario

data class ErrorModel(
    val _id: String,
    val expected: String,
    val actual: String,
    val length: Int,
    val date: String,
    val clientId: String
)