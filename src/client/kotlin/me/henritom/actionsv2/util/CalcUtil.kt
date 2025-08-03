package me.henritom.actionsv2.util

import net.objecthunter.exp4j.ExpressionBuilder
import org.apache.logging.log4j.LogManager

object CalcUtil {

    private val logger = LogManager.getLogger("Actions/Calc")

    fun evaluateExpression(expression: String): Double? {
        return try {
            val expression = ExpressionBuilder(expression).build()
            expression.evaluate()
        } catch (e: Exception) {
            logger.error("Failed to evaluate expression: $expression", e)
            null
        }
    }
}
