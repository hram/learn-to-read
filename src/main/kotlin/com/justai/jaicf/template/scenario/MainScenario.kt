package com.justai.jaicf.template.scenario

import com.justai.jaicf.channel.yandexalice.AliceEvent
import com.justai.jaicf.channel.yandexalice.alice
import com.justai.jaicf.channel.yandexalice.api.alice
import com.justai.jaicf.channel.yandexalice.api.model.Image
import com.justai.jaicf.context.ActionContext
import com.justai.jaicf.helpers.logging.WithLogger
import com.justai.jaicf.model.scenario.Scenario
import com.justai.jaicf.reactions.Reactions

object MainScenario : Scenario(dependencies = listOf(HelperScenario)), WithLogger {

    private val wordsManager = WordsManager()

    init {
        state(stateMain) {
            activators {
                event(AliceEvent.START)
            }

            action {
                logger.info("Action $path")
                MyContext(context).model?.also {
                    reactions.sayWithDelay(random("С возвращением", "Рада снова видеть тебя", "Где ты так долго пропадал?", "Я рада что ты про меня вспомнил", "Ура ты вернулся"))
                    createCard(this, wordsManager.getWord(it.word))
                } ?: run {
                    logger.info("Show menu")
                    reactions.showMenu(MyContext(context))
                }
            }

            state(stateStart) {
                activators {
                    regex("Начать")
                    regex("начать")
                    regex("Старт")
                    regex("старт")
                }

                action {
                    logger.info("Action $path")
                    reactions.sayWithDelay(random("Поздравляю вы сделали правильный выбор", "Ну что, начнем", "Приступим к обучению", "Занятие начинается"))
                    val context = MyContext(context)
                    val word = wordsManager.getFirstWord(context.choosedLength!!)
                    createCard(this, word)
                }
            }

            state(stateSkip) {
                activators {
                    regex("Пропустить")
                    regex("пропустить")
                    regex("Дальше")
                    regex("дальше")
                }

                action {
                    logger.info("Action $path")
                    MyContext(context).also { context ->
                        if (hasNext(context)) {
                            reactions.sayWithDelay("Не унывай. В следующий раз обязательно получится.")
                            createCard(this, wordsManager.getWord(context.model!!.next!!))
                        } else {
                            reactions.run {
                                say("Поздравляю вы прочитали все слова длиной ${context.choosedLength}")
                                buttons(btRepeat, btMenu)
                            }
                        }
                    }
                }
            }

            state(stateMenu) {
                activators {
                    regex("Меню")
                    regex("меню")
                    regex("Назад")
                    regex("назад")
                }

                action {
                    logger.info("Action $path")
                    reactions.showMenu(MyContext(context))
                }
            }

            state(stateRepeat) {
                activators {
                    regex("Повторить")
                    regex("повторить")
                    regex("Ещё раз")
                    regex("ещё раз")
                    regex("Еще раз")
                    regex("еще раз")
                }

                action {
                    logger.info("Action $path")
                    val context = MyContext(context)
                    val word = wordsManager.getFirstWord(context.choosedLength!!)
                    createCard(this, word)
                }
            }
        }

        state(stateExit) {
            activators {
                regex("Завершить")
                regex("завершить")
                regex("Выход")
                regex("выход")
            }

            action {
                logger.info("Action $path")
                reactions.sayRandom("До скорой встречи.", "До связи.")
                reactions.alice?.endSession()
            }
        }

        state(stateFallback, noContext = true) {
            activators {
                catchAll()
            }

            action {
                logger.info("Action $path")
                logger.info("Request ${request.alice?.request}")
                val message = request.alice?.request!!.command
                when (message) {
                    testModeEnterCommand -> {
                        logger.info("Test mode enter")
                        MyContext(context).testMode = true
                    }
                    testModeExitCommand -> {
                        logger.info("Test mode exit")
                        MyContext(context).testMode = false
                    }
                }
                MyContext(context).also { context ->
                    context.model?.also { model ->
                        if (model.word == message) {
                            if (context.wordsLearned == null) {
                                context.wordsLearned = 0
                            }
                            context.wordsLearned = context.wordsLearned!! + 1
                            if (hasNext(context)) {
                                reactions.sayWithDelay(random(*congrats))
                                createCard(this, wordsManager.getWord(model.next!!))
                            } else {
                                reactions.run {
                                    say("Поздравляю вы прочитали все слова длиной ${model.length}")
                                    buttons(btRepeat, btMenu)
                                }
                            }
                        } else {
                            reactions.sayWithDelay(random("Попробуй еще раз", "Что то пошло е так", "Сожалею но нет", "Не торопись. У тебя всё получится", "Всегда можно пропустить слово и перейти к новому."))
                            createCard(this, model)
                        }
                    } ?: run {
                        reactions.showMenu(context)
                    }
                }
            }
        }
    }

    private fun createCard(actionContext: ActionContext, model: WordModel) {
        logger.info("Create card ${model.word}")
        try {
            val context = MyContext(actionContext.context)
            actionContext.reactions.alice?.image(Image(model.imageUrl))
            if (context.wordsLearned == null || context.wordsLearned!! < 10) {
                actionContext.reactions.say("Произнесите слово на картинке")
            }
            actionContext.reactions.buttons(btSkip, btMenu)
            context.model = model
        } catch (e: Exception) {
            sayError(actionContext)
        }
    }

    private fun sayError(actionContext: ActionContext) {
        actionContext.reactions.run {
            say("Ой похоже у меня ошибка в программе.")
            say("Я уже пытаюсь починить ее.")
            say("Пока я разбираюсь в причине давай приступим к другому заданию.")
            if (MyContext(actionContext.context).testMode == true) {
                say(chooseRangeShort)
            } else {
                say(chooseRangeLong)
            }
            say("Произнесите цифру вслух или выберите диапазон.")
            if (MyContext(actionContext.context).testMode == true) {
                actionContext.reactions.buttons(*rangeShort)
            } else {
                actionContext.reactions.buttons(*rangeLong)
            }
            MyContext(actionContext.context).model = null
        }
    }

    private fun hasNext(context: MyContext): Boolean {
        var hasNext = false
        context.model?.also { model ->
            model.next?.apply {
                hasNext = !(context.testMode == true && model.index >= 4)
            }
        }

        return hasNext
    }
}

fun Reactions.sayWithDelay(text: String, delay: Long = 1000) = alice!!.say(text, "$text sil <[$delay]>")