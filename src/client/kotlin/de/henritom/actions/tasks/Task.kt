package de.henritom.actions.tasks

import de.henritom.actions.actions.Action

class Task(action: Action, typeDef: TaskEnum, valueDef: Any? = 0) {

    val id = TaskManager.instance.getNextAvailableID(action)
    val type = typeDef
    var value: Any = valueDef ?: 0

}