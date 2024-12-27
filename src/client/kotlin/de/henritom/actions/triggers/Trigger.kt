package de.henritom.actions.triggers

import de.henritom.actions.actions.Action

class Trigger(action: Action, typeDef: TriggerEnum, valueDef: Any? = 0) {

    val id = TriggerManager.instance.getNextAvailableID(action)
    val type = typeDef
    var value: Any = valueDef ?: 0

}