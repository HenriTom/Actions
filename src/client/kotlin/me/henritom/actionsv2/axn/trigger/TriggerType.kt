package me.henritom.actionsv2.axn.trigger

class TriggerType(val type: String, val description: String, val requiredData: List<TriggerSetting> = listOf()) {
    init {
        TriggerRegistry.registerTriggerType(this)
    }
}