package de.henritom.actions.actions

import de.henritom.actions.trigger.Trigger

abstract class Action(val name: String) {

    val id: Int = ActionManager.instance.getNextAvailableID()
    val triggers = MutableList(1) { Trigger.CALL }
    var author = "%Unknown%"

    abstract fun call()

}