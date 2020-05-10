package com.justai.jaicf.template.scenario

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.UpdateOptions
import org.bson.Document

class SuccessManager() : BaseManager() {

    private val mapper = jacksonObjectMapper().enableDefaultTyping()

    private val collection: MongoCollection<Document> = client.getDatabase(uri.database!!).getCollection("success")

    fun getAllSuccess(): List<SuccessModel> {
        val list: MutableList<SuccessModel> = mutableListOf()
        collection.find().forEach { item ->
            val model = mapper.readValue(item.toJson(), SuccessModel::class.java)
            list.add(model)
        }
        return list
    }

    fun insertOne(model: SuccessModel) {
        val doc = Document.parse(mapper.writeValueAsString(model))
        collection.insertOne(doc)
    }

    fun replaceOne(model: SuccessModel) {
        val doc = Document.parse(mapper.writeValueAsString(model))
        collection.replaceOne(eq("_id", model._id), doc, UpdateOptions().upsert(true))
    }

    fun deleteById(id: String) {
        collection.deleteOne(eq("_id", id))
    }
}