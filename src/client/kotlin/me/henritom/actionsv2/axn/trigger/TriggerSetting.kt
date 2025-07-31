package me.henritom.actionsv2.axn.trigger

data class TriggerSetting(
    val name: String,
    val description: String,
    val type: String,
    val defaultValue: Any? = null,
    val required: Boolean = true
)