package me.henritom.actionsv2.axn.task.impl

import me.henritom.actionsv2.axn.AxnContext
import me.henritom.actionsv2.axn.task.AxnTask
import me.henritom.actionsv2.variables.VariableHelper

class LogTask: AxnTask("log") {

    override suspend fun execute(context: AxnContext, extraData: Map<String, Any>): Boolean {
        val message = VariableHelper.replaceStr(data["message"]?.toString() ?: "No message provided")

        when (data["level"]?.toString()?.lowercase()) {
            "debug" -> context.logger.debug(message)
            "error" -> context.logger.error(message)
            "fatal" -> context.logger.fatal(message)
            "trace" -> context.logger.trace(message)
            "warn"  -> context.logger.warn(message)
            else    -> context.logger.info(message)
        }

        return true
    }
}