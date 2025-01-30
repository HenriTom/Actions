package de.henritom.actions.triggers

import de.henritom.actions.actions.Action

class Trigger(val action: Action, typeDef: TriggerEnum, valueDef: Any? = 0) {

    var id = TriggerManager.instance.getNextAvailableID(action)
    val type = typeDef
    var value: Any = valueDef ?: 0

}