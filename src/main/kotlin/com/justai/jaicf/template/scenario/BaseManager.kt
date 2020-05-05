package com.justai.jaicf.template.scenario

import com.mongodb.MongoClient
import com.mongodb.MongoClientURI

open class BaseManager {

    private val login = System.getenv("MONGODB_LOGIN") ?: ""

    private val password = System.getenv("MONGODB_PASSWORD") ?: ""

    protected val uri = MongoClientURI("mongodb://$login:$password@ds055762.mlab.com:55762/heroku_9w2h7mvv")

    protected val client = MongoClient(uri)
}