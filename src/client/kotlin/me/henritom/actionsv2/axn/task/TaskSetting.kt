package me.henritom.actionsv2.axn.task

data class TaskSetting(
    val name: String,
    val description: String,
    val type: String,
    val defaultValue: Any? = null,
    val required: Boolean = true
)