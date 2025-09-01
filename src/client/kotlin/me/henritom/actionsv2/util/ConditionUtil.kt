package me.henritom.actionsv2.util

import me.henritom.actionsv2.variables.VariableHelper
import org.apache.logging.log4j.LogManager

object ConditionUtil {

    private val logger = LogManager.getLogger("Actions/Condition")

    fun evaluateCondition(condition: String): Boolean {
        val replaced = VariableHelper.replaceStr(condition, null)
        val parts = replaced.trim().split(" ")

        return evalParts(parts)
    }

    private fun parseValue(value: String): Any = when {
        value.equals("true", ignoreCase = true) -> true
        value.equals("false", ignoreCase = true) -> false
        value.toDoubleOrNull() != null -> value.toDouble()
        else -> value
    }

    private fun checkSimpleCondition(parts: List<String>): Boolean {
        if (parts.size < 3)
            return false

        val varA = parseValue(parts[0])
        val operator = parts[1]
        val varB = parseValue(parts[2])

        return checkCondition(varA, operator, varB)
    }

    private fun combine(current: Boolean?, next: Boolean, operator: String?): Boolean {
        return when (operator) {
            "and" -> (current ?: true) && next
            "or" -> (current ?: false) || next
            else -> next
        }
    }

    private fun evalParts(parts: List<String>): Boolean {
        if (parts.isEmpty())
            return false

        var result: Boolean? = null
        var pendingOperator: String? = null
        var index = 0

        while (index < parts.size) {
            var negate = false
            var part = parts[index]

            if (part.startsWith("!")) {
                negate = true
                part = part.removePrefix("!")
            }

            if (part.equals("&&", ignoreCase = true) || part.equals("and", ignoreCase = true)) {
                pendingOperator = "and"
                index++
                continue
            }

            if (part.equals("||", ignoreCase = true) || part.equals("or", ignoreCase = true)) {
                pendingOperator = "or"
                index++
                continue
            }

            if (index + 2 >= parts.size) {
                logger.error("Incomplete condition at index $index: ${parts.drop(index)}")
                return false
            }

            val conditionValue = checkSimpleCondition(listOf(part, parts[index + 1], parts[index + 2]))
            val finalValue = if (negate) !conditionValue else conditionValue

            result = combine(result, finalValue, pendingOperator)

            index += 3
        }

        return result ?: false
    }

    private fun normalizeOperator(operator: String): Pair<String, Boolean> {
        val negations = operator.takeWhile { it == '!' }.length
        val baseOp = operator.drop(negations)

        val negated = negations % 2 == 1

        return baseOp.lowercase() to negated
    }

    private fun checkCondition(varA: Any, operator: String, varB: Any): Boolean {
        val (op, negated) = normalizeOperator(operator)

        val result = when (varA) {
            is Double if varB is Double -> when (op) {
                "<" -> varA < varB
                ">" -> varA > varB
                "<=" -> varA <= varB
                ">=" -> varA >= varB
                "==", "=" -> varA == varB
                "!=" -> varA != varB
                else -> false
            }

            is Boolean if varB is Boolean -> when (op) {
                "==", "=" -> varA == varB
                "!=" -> varA != varB
                else -> false
            }

            is String if varB is String -> when (op) {
                "==", "=" -> varA == varB
                "!=" -> varA != varB
                "equals" -> varA == varB
                "notequals" -> varA != varB
                "contains" -> varA.contains(varB)
                "notcontains" -> !varA.contains(varB)
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

        return if (negated) !result else result
    }
}
