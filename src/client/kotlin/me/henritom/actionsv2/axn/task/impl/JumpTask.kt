package me.henritom.actionsv2.axn.task.impl

import me.henritom.actionsv2.axn.AxnContext
import me.henritom.actionsv2.axn.task.AxnTask

class JumpTask: AxnTask("jump") {

    override suspend fun execute(context: AxnContext, extraData: Map<String, Any>): Boolean {
        val destination = (data["destination"]?.toString()?.toDoubleOrNull() ?: 0.0).toInt().coerceAtLeast(0)

        context.scheduler.jumpTo(destination)

        return true
    }
}