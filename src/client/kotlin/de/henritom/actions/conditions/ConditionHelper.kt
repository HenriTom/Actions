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
                    else -> false
                }

                varA is Boolean && varB is Boolean -> when (operator.lowercase()) {
                    "==" -> varA == varB
                    else -> false
                }

                varA is String && varB is String -> when (operator.lowercase()) {
                    "==" -> varA == varB

                    "equals" -> varA == varB
                    "contains" -> varA.contains(varB.toString())
                    "startswith" -> varA.startsWith(varB.toString())
                    "endswith" -> varA.endsWith(varB.toString())

                    "equalsignorecase" -> varA.equals(varB.toString(), ignoreCase = true)
                    "containsignorecase" -> varA.contains(varB.toString(), ignoreCase = true)
                    "startswithignorecase" -> varA.startsWith(varB.toString(), ignoreCase = true)
                    "endswithignorecase" -> varA.endsWith(varB.toString(), ignoreCase = true)

                    else -> false
                }

                else -> false
            }
        }
    }
}
