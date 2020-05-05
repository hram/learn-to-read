package com.justai.jaicf.template.scenario

import com.justai.jaicf.context.BotContext

class MyContext(context: BotContext) {
    var choosedLength: Int? by context.client
    var model: WordModel? by context.client
    var testMode: Boolean? by context.client
}