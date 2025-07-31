package me.henritom.actionsv2.axn.trigger

class TriggerType(type: String, description: String, requiredData: List<TriggerSetting> = listOf()) {
    init {
        TriggerRegistry.registerTriggerType(this)
    }
}