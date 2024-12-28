package de.henritom.actions.actions

import de.henritom.actions.tasks.Task
import de.henritom.actions.triggers.Trigger

abstract class Action(val name: String) {

    var id: Int = ActionManager.instance.getNextAvailableID()
    val triggers = mutableListOf<Trigger>()
    val tasks = mutableListOf<Task>()
    var author = "%Unknown%"

    abstract fun call()

}