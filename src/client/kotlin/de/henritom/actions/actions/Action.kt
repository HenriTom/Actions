package de.henritom.actions.actions

import de.henritom.actions.triggers.Trigger

abstract class Action(val name: String) {

    val id: Int = ActionManager.instance.getNextAvailableID()
    val triggers = mutableListOf<Trigger>()
    var author = "%Unknown%"

    abstract fun call()

}