package me.henritom.actionsv2.axn.task.impl

import me.henritom.actionsv2.axn.AxnContext
import me.henritom.actionsv2.axn.task.AxnTask
import me.henritom.actionsv2.util.ConditionUtil

class IfTask : AxnTask("if") {
    override suspend fun execute(context: AxnContext, extraData: Map<String, Any>): Boolean {
        val condition = data["condition"]?.toString() ?: return false
        val length = (data["length"]?.toString()?.toDoubleOrNull() ?: 1.0).toInt().coerceAtLeast(1)

        if (!ConditionUtil.evaluateCondition(condition))
            context.scheduler.jumpTo(context.scheduler.currentTaskIndex + length)

        return true
    }
}