package me.henritom.actionsv2.axn.task.impl

import me.henritom.actionsv2.axn.AxnContext
import me.henritom.actionsv2.axn.task.AxnTask

class LogTask: AxnTask("log") {

    override suspend fun execute(context: AxnContext, extraData: Map<String, Any>): Boolean {
        when (data["level"]?.toString()?.lowercase()) {
            "debug" -> context.logger.debug(data["message"]?.toString() ?: "No message provided")
            "error" -> context.logger.error(data["message"]?.toString() ?: "No message provided")
            "fatal" -> context.logger.fatal(data["message"]?.toString() ?: "No message provided")
            "trace" -> context.logger.trace(data["message"]?.toString() ?: "No message provided")
            "warn"  -> context.logger.warn(data["message"]?.toString() ?: "No message provided")
            else    -> context.logger.info(data["message"]?.toString() ?: "No message provided")
        }

        return true
    }
}