package de.henritom.actions.conditions

import de.henritom.actions.util.MessageUtil

class ConditionHelper {
    companion object {
        fun checkIf(condition: String): Boolean {
            val conditionParts = MessageUtil(null).translateVariables(condition).split(" ")

            println("Checking condition: ")
            for (conditionPart in conditionParts)
                print("$conditionPart ")
            println()

            if (conditionParts.size != 3) {
                if (conditionParts.size < 7)
                    return false

                if (conditionParts[3] == "&&" || conditionParts[3] == "and")
                    return checkIf(conditionParts[0] + " " + conditionParts[1] + " " + conditionParts[2]) && checkIf(conditionParts[4] + " " + conditionParts[5] + " " + conditionParts[6])

                if (conditionParts[3] == "||" || conditionParts[3] == "or")
                    return checkIf(conditionParts[0] + " " + conditionParts[1] + " " + conditionParts[2]) || checkIf(conditionParts[4] + " " + conditionParts[5] + " " + conditionParts[6])

                return false
            }

            fun parseValue(value: String): Any = when {
                value.equals("true", ignoreCase = true) -> true
                value.equals("false", ignoreCase = true) -> false
                value.toDoubleOrNull() != null -> value.toDouble()
                else -> value
            }

            val varA = parseValue(conditionParts[0])
            val operator = conditionParts[1]
            val varB = parseValue(conditionParts[2])

            return if (operator.startsWith("!"))
                !checkCondition(varA, operator.substring(1), varB)
            else
                checkCondition(varA, operator, varB)
        }

        private fun checkCondition(varA: Any, operator: String, varB: Any): Boolean {
            return when {
                varA is Double && varB is Double -> when (operator.lowercase()) {
                    "<" -> varA < varB
                    ">" -> varA > varB
                    "<=" -> varA <= varB
                    ">=" -> varA >= varB
                    "==" -> varA == varB
                    "=" -> varA == varB
                    else -> false
                }

                varA is Boolean && varB is Boolean -> when (operator.lowercase()) {
                    "==" -> varA == varB
                    "=" -> varA == varB
                    else -> false
                }

                varA is String && varB is String -> when (operator.lowercase()) {
                    "==" -> varA == varB
                    "=" -> varA == varB

                    "equals" -> varA == varB
                    "contains" -> varA.contains(varB)
                    "startswith" -> varA.startsWith(varB)
                    "endswith" -> varA.endsWith(varB)

                    "equalsignorecase" -> varA.equals(varB, ignoreCase = true)
                    "containsignorecase" -> varA.contains(varB, ignoreCase = true)
                    "startswithignorecase" -> varA.startsWith(varB, ignoreCase = true)
                    "endswithignorecase" -> varA.endsWith(varB, ignoreCase = true)

                    else -> false
                }

                else -> false
            }
        }
    }
}
