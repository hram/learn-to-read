package com.justai.jaicf.template.scenario

import com.justai.jaicf.context.manager.mongo.MongoBotContextManager

class ContextManager : BaseManager() {

    val manager = MongoBotContextManager(client.getDatabase(uri.database!!).getCollection("contexts"))
}