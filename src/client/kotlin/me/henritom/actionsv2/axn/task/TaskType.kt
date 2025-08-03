package me.henritom.actionsv2.axn.task

class TaskType(val type: String, val description: String, val requiredData: List<TaskSetting> = listOf()) {
    init {
        TaskRegistry.registerTaskType(this)
    }
}