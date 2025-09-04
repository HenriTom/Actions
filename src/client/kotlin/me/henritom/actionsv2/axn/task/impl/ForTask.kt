package me.henritom.actionsv2.axn.task.impl

import me.henritom.actionsv2.axn.AxnContext
import me.henritom.actionsv2.axn.task.AxnTask

class ForTask : AxnTask("for") {
    override suspend fun execute(context: AxnContext, extraData: Map<String, Any>): Boolean {
        var times = (data["times"]?.toString()?.toDoubleOrNull() ?: 1.0).toInt().coerceAtLeast(1)
        val length = (data["length"]?.toString()?.toDoubleOrNull() ?: 1.0).toInt().coerceAtLeast(1)

        val thisTask = context.scheduler.currentTaskIndex
        val loopEnd = thisTask + length

        repeat(times) {
            for (taskIndex in (thisTask + 1) until loopEnd + 1)
                context.action.tasks[taskIndex].execute(context, extraData)
        }

        context.scheduler.jumpTo(loopEnd)

        return true
    }
}