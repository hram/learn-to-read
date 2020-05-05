package com.justai.jaicf.template.scenario

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.UpdateOptions
import org.bson.Document

class WordsManager() : BaseManager() {

    private val mapper = jacksonObjectMapper().enableDefaultTyping()

    private val collection: MongoCollection<Document> = client.getDatabase(uri.database!!).getCollection("words")

    fun getWords(length: Int): List<WordModel> {
        val list: MutableList<WordModel> = mutableListOf()
        collection.find(Filters.eq("length", length)).forEach { item ->
            val model = mapper.readValue(item.toJson(), WordModel::class.java)
            list.add(model)
        }
        return list
    }

    fun getWord(word: String): WordModel {
        return mapper.readValue(collection.find(Filters.eq("_id", word)).first().toJson(), WordModel::class.java)
    }

    fun getFirstWord(length: Int): WordModel {
        return mapper.readValue(collection.find(and(eq("length", length), eq("index", 0))).first().toJson(), WordModel::class.java)
    }

    fun insertOne(model: WordModel) {
        val doc = Document.parse(mapper.writeValueAsString(model))
        collection.insertOne(doc)
    }

    fun replaceOne(model: WordModel) {
        val doc = Document.parse(mapper.writeValueAsString(model))
        collection.replaceOne(Filters.eq("_id", model._id), doc, UpdateOptions().upsert(true))
    }
}