package me.henritom.actionsv2.axn.task.impl

import me.henritom.actionsv2.axn.AxnContext
import me.henritom.actionsv2.axn.task.AxnTask
import me.henritom.actionsv2.util.ConditionUtil

class WhileTask : AxnTask("while") {
    override suspend fun execute(context: AxnContext, extraData: Map<String, Any>): Boolean {
        val condition = data["condition"]?.toString() ?: return false
        val length = (data["length"]?.toString()?.toDoubleOrNull() ?: 1.0).toInt().coerceAtLeast(1)
        val max_iterations = (data["max_iterations"]?.toString()?.toDoubleOrNull() ?: 0.0).toInt().coerceAtLeast(0)

        val thisTask = context.scheduler.currentTaskIndex
        val loopEnd = thisTask + length

        var iterations = 0
        while(ConditionUtil.evaluateCondition(condition) && (iterations < max_iterations || max_iterations == 0)) {
            for (taskIndex in (thisTask + 1) until loopEnd + 1)
                context.action.tasks[taskIndex].execute(context, extraData)

            iterations++
        }

        context.scheduler.jumpTo(loopEnd)

        return true
    }
}