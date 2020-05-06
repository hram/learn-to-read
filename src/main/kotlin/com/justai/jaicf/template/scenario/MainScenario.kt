package com.justai.jaicf.template.scenario

import com.justai.jaicf.channel.yandexalice.AliceEvent
import com.justai.jaicf.channel.yandexalice.alice
import com.justai.jaicf.channel.yandexalice.api.alice
import com.justai.jaicf.context.ActionContext
import com.justai.jaicf.model.scenario.Scenario

object MainScenario : Scenario() {

    //private val contextManager = ContextManager()

    private val wordsManager = WordsManager()

    private const val testModeEnterCommand = "перейти в тестовый режим"

    private const val testModeExitCommand = "выйти из тестового режима"

    private const val chooseLength = "Уточните выбор. Произнесите цифру или выберите из списка."

    private const val chooseRangeShort = "Выберите длину слова от 1 до 5."

    private const val chooseRangeLong = "Выберите длину слова от 1 до 30."

    private const val btMenu = "Меню"

    private const val btRepeat = "Ещё раз"

    private const val btStart = "Старт"

    private const val btSkip = "Пропустить"

    private const val btExit = "Выход"

    private const val stateMain = "main"

    private const val stateMenu = "menu"

    private const val stateStart = "start"

    private const val stateSkip = "skip"

    private const val stateRepeat = "repeat"

    private const val stateExit = "exit"

    private const val stateFallback = "fallback"

    private const val stateChooseLength = "chooseLength"

    private const val stateChooseRange = "chooseRange"

    private val regexChooseRange = """(\d+) - (\d+)""".toRegex()

    private val regexChooseLength = "^[0-9]+\$".toRegex()

    private val rangeShort = arrayOf("1 - 5", btExit)

    private val rangeLong = arrayOf("1 - 5", "6 - 10", "11 - 15", "16 - 20", "21 - 25", "26 - 30", btExit)

    init {
        state(stateMain) {
            activators {
                event(AliceEvent.START)
            }

            action {
                MyContext(context).model?.also {
                    createCard(this, wordsManager.getWord(it.word))
                } ?: run {
                    sayHello(this)
                }
            }

            state(stateMenu) {
                activators {
                    regex("меню")
                    regex("назад")
                }

                action {
                    reactions.go("/$stateMenu")
                }
            }
        }

        state(stateSkip) {
            activators {
                regex("Пропустить")
            }

            action {
                MyContext(context).also { context ->
                    if (hasNext(context)) {
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

        state(stateRepeat) {
            activators {
                regex("повторить")
                regex("Ещё раз")
                regex("ещё раз")
                regex("еще раз")
            }

            action {
                val context = MyContext(context)
                val word = wordsManager.getFirstWord(context.choosedLength!!)
                createCard(this, word)
            }
        }

        state(stateMenu) {
            activators {
                regex("меню")
                regex("назад")
            }

            action {
                sayHello(this)
            }
        }

        state(stateExit) {
            activators {
                regex("Завершить")
                regex("Выход")
            }

            action {
                reactions.sayRandom("До скорой встречи.", "До связи.")
                reactions.alice?.endSession()
            }
        }

        state(stateChooseLength) {
            activators {
                regex(regexChooseLength)
            }

            action {
                val context = MyContext(context)
                val message = request.alice?.request!!.command
                reactions.run {
                    say("Вы выбрали размер слова $message.")
                    say("Чтобы приступить к изучению скажите - старт")
                    say("Чтобы выбрать другой размер слова скажите - меню")
                    buttons(btStart, btMenu)
                    context.choosedLength = message.toInt()
                }
            }

            state(stateStart) {
                activators {
                    regex("начать")
                    regex("старт")
                }

                action {
                    val context = MyContext(context)
                    val word = wordsManager.getFirstWord(context.choosedLength!!)
                    createCard(this, word)
                }
            }

            state(stateMenu) {
                activators {
                    regex("меню")
                    regex("назад")
                }

                action {
                    reactions.go("/$stateMenu")
                }
            }
        }

        state(stateFallback, noContext = true) {
            activators {
                catchAll()
            }

            action {
                val message = request.alice?.request!!.command
                when (message) {
                    testModeEnterCommand -> MyContext(context).testMode = true
                    testModeExitCommand -> MyContext(context).testMode = false
                }
                MyContext(context).also { context ->
                    context.model?.also { model ->
                        if (model.word == message) {
                            if (hasNext(context)) {
                                reactions.sayRandom("Молодец", "Правильно", "Ура")
                                createCard(this, wordsManager.getWord(model.next!!))
                            } else {
                                reactions.run {
                                    say("Поздравляю вы прочитали все слова длиной ${model.length}")
                                    buttons(btRepeat, btMenu)
                                }
                            }
                        } else {
                            reactions.say("Попробуй еще раз")
                            createCard(this, model)
                        }
                    } ?: run {
                        sayHello(this)
                    }
                }
            }
        }

        state(stateChooseRange) {
            activators {
                regex(regexChooseRange)
            }

            action {
                chooseLength(this)
            }
        }
    }

    private fun chooseLength(actionContext: ActionContext) {
        val res = regexChooseRange.find(actionContext.request.alice?.request!!.command)!!
        val from = res.groups[1]!!.value.toInt()
        val to = res.groups[2]!!.value.toInt()
        val buttonsArray = mutableListOf<String>()
        (from..to).forEach {
            buttonsArray.add(it.toString())
        }
        buttonsArray.add(btMenu)
        actionContext.reactions.run {
            say(chooseLength)
            buttons(*buttonsArray.toTypedArray())
        }
    }

    private fun createCard(actionContext: ActionContext, model: WordModel) {
        try {
            val context = MyContext(actionContext.context)
            actionContext.reactions.alice?.image("http://hram0v.com/learn-to-read/${model.imageUrl}.png")
            actionContext.reactions.say("Произнесите слово на картинке")
            actionContext.reactions.buttons(btSkip, btMenu)
            context.model = model
            //contextManager.manager.saveContext(actionContext.context)
        } catch (e: Exception) {
            sayError(actionContext)
        }
    }

    private fun sayHello(actionContext: ActionContext) {
        actionContext.reactions.run {
            say("Приветствую.")
            say("Я помогу вам научиться читать.")
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