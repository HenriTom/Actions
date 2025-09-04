package me.henritom.actionsv2.axn.task.impl

import me.henritom.actionsv2.axn.AxnContext
import me.henritom.actionsv2.axn.task.AxnTask
import me.henritom.actionsv2.util.ConditionUtil

class ElseTask : AxnTask("if") {
    override suspend fun execute(context: AxnContext, extraData: Map<String, Any>): Boolean {
        var taskIndex = context.scheduler.currentTaskIndex
        var condition = "1 != 1"

        while (taskIndex != -1) {
            val task = context.action.tasks.get(taskIndex)
            if (task is IfTask) {
                condition = task.data["condition"]?.toString() ?: "1 != 1"
                break
            } else {
                taskIndex--
            }
        }

        val length = (data["length"]?.toString()?.toDoubleOrNull() ?: 1.0).toInt().coerceAtLeast(1)

        if (ConditionUtil.evaluateCondition(condition))
            context.scheduler.jumpTo(context.scheduler.currentTaskIndex + length)

        return true
    }
}