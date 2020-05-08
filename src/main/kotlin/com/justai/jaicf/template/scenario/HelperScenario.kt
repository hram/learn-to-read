package com.justai.jaicf.template.scenario

import com.justai.jaicf.channel.yandexalice.api.alice
import com.justai.jaicf.helpers.logging.WithLogger
import com.justai.jaicf.model.scenario.Scenario
import com.justai.jaicf.reactions.Reactions

object HelperScenario : Scenario(), WithLogger {

    private val wordsManager = WordsManager()

    init {
        state(stateHelper) {

            state(stateMenu) {
                activators {
                    catchAll()
                }

                action {
                    logger.info("Action $path")
                    reactions.showMenu(MyContext(context))
                }

                state(stateChooseRange) {
                    activators {
                        regex(regexChooseRange)
                    }

                    action {
                        logger.info("Action $path")
                        val res = regexChooseRange.find(request.alice?.request!!.command)!!
                        val from = res.groups[1]!!.value.toInt()
                        val to = res.groups[2]!!.value.toInt()
                        val buttonsArray = mutableListOf<String>()
                        (from..to).forEach {
                            buttonsArray.add(it.toString())
                        }
                        buttonsArray.add(btMenu)
                        reactions.run {
                            say(chooseLength)
                            buttons(*buttonsArray.toTypedArray())
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
                }

                state(stateChooseLength) {
                    activators {
                        regex(regexChooseLength)
                    }

                    action {
                        logger.info("Action $path")
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
                            regex("Начать")
                            regex("начать")
                            regex("Старт")
                            regex("старт")
                        }

                        action {
                            logger.info("Action $path")
                            val context = MyContext(context)
                            context.model = wordsManager.getFirstWord(context.choosedLength!!)
                            reactions.goBack()
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
                }
            }
        }
    }
}

fun Reactions.showMenu(context: MyContext) {
    say("Приветствую.")
    say("Я помогу вам научиться читать.")
    if (context.testMode == true) {
        say(chooseRangeShort)
    } else {
        say(chooseRangeLong)
    }
    say("Произнесите цифру вслух или выберите диапазон.")
    if (context.testMode == true) {
        buttons(*rangeShort)
    } else {
        buttons(*rangeLong)
    }
    context.model = null
    changeState("/$stateHelper/$stateMenu", "/main/start")
}