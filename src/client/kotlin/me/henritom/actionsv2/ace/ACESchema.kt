package me.henritom.actionsv2.ace

import me.henritom.actionsv2.axn.task.TaskType
import me.henritom.actionsv2.axn.trigger.TriggerType

data class ACESchema(
    val schemaVersion: Int,
    val triggers: List<TriggerType>,
    val tasks: List<TaskType>
)
