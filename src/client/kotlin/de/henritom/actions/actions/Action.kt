package de.henritom.actions.actions

import de.henritom.actions.tasks.Task
import de.henritom.actions.triggers.Trigger
import java.io.File

abstract class Action(val name: String) {

    var id: Int = ActionManager.instance.getNextAvailableID()
    val triggers = mutableListOf<Trigger>()
    val tasks = mutableListOf<Task>()
    var author = "%Unknown%"
    var file: File? = null

    abstract fun call()

}