package com.justai.jaicf.template.scenario

import com.justai.jaicf.api.QueryBotRequest
import com.justai.jaicf.channel.yandexalice.api.AliceBotRequest
import com.justai.jaicf.channel.yandexalice.api.Request
import com.justai.jaicf.channel.yandexalice.api.Session
import com.justai.jaicf.test.ScenarioTest
import org.junit.jupiter.api.Test

class MainScenarioTest : ScenarioTest(MainScenario) {

    @Test
    fun `main query test`() {
        withBotContext { client["name"] = "some name" }
        queryAlice("") endsWithState "/main"
    }

    @Test
    fun `report query test`() {
        val query = "докладываю ты мудак 2"
        queryAlice(query) responds "Спасибо.\n" +
                "Ваш донос \"$query\" зарегистрирован под номером 1000.\n" +
                "У вас есть еще какая-нибудь информация?"
        queryAlice(query) endsWithState "/report"
    }

    @Test
    fun `Greets a known user`() {
        //withBotContext { client["name"] = "some name" }
        val query = "hi"
        queryAlice(query) responds "Не тратьте моего времени зря. Начните сообщение со слова \"Докладываю\"."
        queryAlice(query) endsWithState "/"
    }

    private fun queryAlice(query: String) = process(
        AliceBotRequest(
            version = "1.1",
            session = Session(
                newSession = true,
                messageId = 1234,
                sessionId = "sessionId",
                skillId = "skillId",
                userId = "userId"
            ),
            request = Request(
                command = query,
                type = "type",
                nlu = Request.Nlu(emptyList(), emptyList()),
                originalUtterance = "originalUtterance"
            )
        )
    )
}