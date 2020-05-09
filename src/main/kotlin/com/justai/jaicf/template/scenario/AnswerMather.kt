package com.justai.jaicf.template.scenario

class AnswerMather {

    fun isMatch(expected: String, actual: String): Boolean {
        if (isEquals(expected, actual)) {
            return true
        }

        // если слово произнесли дважды
        val words = actual.split(" ")
        if (words.size == 2 && isEquals(words[0], words[1]) && isEquals(words[0], expected)) {
            return true
        }

        // буква Г пришло Где
        if (expected.length == 1 && actual.length < 4 && isEquals(expected[0].toString(), actual[0].toString())) {
            return true
        }

        // буква Ё пришло Е
        if (isEquals(expected, "ё") && isEquals(actual, "е")) {
            return true
        }

        // буква Й пришло И
        if (isEquals(expected, "й") && isEquals(actual, "и")) {
            return true
        }

        // буква Щ пришло ЧЕ
        if (isEquals(expected, "щ") && isEquals(actual, "че")) {
            return true
        }

        // буква Ъ пришло твердый знак
        if (isEquals(expected, "ъ") && isEquals(actual, "твердый знак")) {
            return true
        }

        // буква Ь пришло мягкий знак
        if (isEquals(expected, "ь") && isEquals(actual, "мягкий знак")) {
            return true
        }

        // буква Ы пришло Э
        if (isEquals(expected, "ы") && isEquals(actual, "э")) {
            return true
        }

        // буква Ы пришло И
        if (isEquals(expected, "ы") && isEquals(actual, "и")) {
            return true
        }

        if (expected.length == 2) {
            // слово НА пришло ДА
            if (isEquals(expected, "на") && isEquals(actual, "да")) {
                return true
            }

            // слово ЖЕ пришло Ж
            if (isEquals(expected, "же") && isEquals(actual, "ж")) {
                return true
            }

            // слово БЫ пришло Б
            if (isEquals(expected, "бы") && isEquals(actual, "б")) {
                return true
            }

            // слово ВТ пришло ВОТ
            if (isEquals(expected, "вт") && isEquals(actual, "вот")) {
                return true
            }

            // слово ВТ пришло ВК
            if (isEquals(expected, "вт") && isEquals(actual, "вк")) {
                return true
            }

            // слово ЯС пришло ЯЗ
            if (isEquals(expected, "яс") && isEquals(actual, "яз")) {
                return true
            }
        }

        if (expected.length == 3) {
            // слово ПРО пришло PRO
            if (isEquals(expected, "про") && isEquals(actual, "pro")) {
                return true
            }
        }

        return false
    }

    private fun isEquals(expected: String, actual: String) = expected.toLowerCase() == actual.toLowerCase()
}