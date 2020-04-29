package com.justai.jaicf.template.scenario

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import org.bson.Document

class WordsManager(private val collection: MongoCollection<Document>) {

    private val mapper = jacksonObjectMapper().enableDefaultTyping()

    fun getWords(length: Int): List<WordModel> {
        val list: MutableList<WordModel> = mutableListOf()
        collection.find(Filters.eq("length", length))
            .forEach { item -> list.add(mapper.readValue(item.toJson(), WordModel::class.java)) }
        return list
    }
}