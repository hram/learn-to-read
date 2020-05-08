package com.justai.jaicf.template.scenario

internal const val testModeEnterCommand = "перейти в тестовый режим"

internal const val testModeExitCommand = "выйти из тестового режима"

internal const val chooseLength = "Уточните выбор. Произнесите цифру или выберите из списка."

internal const val chooseRangeShort = "Выберите длину слова от 1 до 5."

internal const val chooseRangeLong = "Выберите длину слова от 1 до 20."

internal const val btMenu = "Меню"

internal const val btRepeat = "Ещё раз"

internal const val btStart = "Старт"

internal const val btSkip = "Пропустить"

internal const val btExit = "Выход"

internal const val stateMain = "main"

internal const val stateHelper = "helper"

internal const val stateMenu = "menu"

internal const val stateStart = "start"

internal const val stateSkip = "skip"

internal const val stateRepeat = "repeat"

internal const val stateExit = "exit"

internal const val stateFallback = "fallback"

internal const val stateChooseLength = "chooseLength"

internal const val stateChooseRange = "chooseRange"

internal val regexChooseRange = """(\d+) - (\d+)""".toRegex()

internal val regexChooseLength = "^[0-9]+\$".toRegex()

internal val rangeShort = arrayOf("1 - 5", btExit)

internal val rangeLong = arrayOf("1 - 5", "6 - 10", "11 - 15", "16 - 20", btExit)

internal val congrats = arrayOf("Молодец", "Правильно", "Ура", "Ты справлся", "Великолепно", "Восхитительно")