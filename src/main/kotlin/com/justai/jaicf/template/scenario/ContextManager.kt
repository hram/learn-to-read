package com.justai.jaicf.template.scenario

import com.justai.jaicf.context.BotContext
import com.justai.jaicf.context.manager.BotContextManager
import com.justai.jaicf.context.manager.mongo.MongoBotContextManager

class ContextManager : BaseManager(), BotContextManager {

    private val manager = MongoBotContextManager(client.getDatabase(uri.database!!).getCollection("contexts"))

    override fun loadContext(clientId: String): BotContext {
        return manager.loadContext(clientId)
    }

    override fun saveContext(botContext: BotContext) {
        manager.saveContext(botContext)
    }
}