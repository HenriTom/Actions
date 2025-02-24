package de.henritom.actions.conditions

import de.henritom.actions.util.MessageUtil

class ConditionHelper {
    companion object {
        fun checkIf(condition: String): Boolean {
            val conditionParts = MessageUtil(null).translateVariables(condition).split(" ")

            if (conditionParts.size != 3)
                return false

            fun parseValue(value: String): Any = when {
                value.equals("true", ignoreCase = true) -> true
                value.equals("false", ignoreCase = true) -> false
                value.toDoubleOrNull() != null -> value.toDouble()
                else -> value
            }

            val varA = parseValue(conditionParts[0])
            val operator = conditionParts[1]
            val varB = parseValue(conditionParts[2])

            return when {
                varA is Double && varB is Double -> when (operator) {
                    "<" -> varA < varB
                    ">" -> varA > varB
                    "<=" -> varA <= varB
                    ">=" -> varA >= varB
                    "==" -> varA == varB
                    "!=" -> varA != varB
                    else -> false
                }

                varA is Boolean && varB is Boolean -> when (operator) {
                    "==" -> varA == varB
                    "!=" -> varA != varB
                    else -> false
                }

                varA is String && varB is String -> when (operator) {
                    "==" -> varA == varB
                    "!=" -> varA != varB
                    else -> false
                }

                else -> false
            }
        }
    }
}
