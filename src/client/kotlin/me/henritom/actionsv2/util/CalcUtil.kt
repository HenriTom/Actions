package me.henritom.actionsv2.util

import me.henritom.actionsv2.variables.VariableHelper
import net.objecthunter.exp4j.ExpressionBuilder
import org.apache.logging.log4j.LogManager

object CalcUtil {

    private val logger = LogManager.getLogger("Actions/Calc")

    fun evaluateExpression(expression: String): Double? {
        return try {
            ExpressionBuilder(VariableHelper.replaceStr(expression)).build().evaluate()
        } catch (e: Exception) {
            logger.error("Failed to evaluate expression: $expression", e)
            null
        }
    }
}
