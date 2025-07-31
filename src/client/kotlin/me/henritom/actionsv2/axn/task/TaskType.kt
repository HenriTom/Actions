package me.henritom.actionsv2.axn.task

class TaskType(type: String, description: String, requiredData: List<TaskSetting> = listOf()) {
    init {
        TaskRegistry.registerTaskType(this)
    }
}