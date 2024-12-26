package de.henritom.actions.actions

import de.henritom.actions.trigger.Trigger

class ActionEditManager {
    companion object {
        val instance = ActionEditManager()
    }

    fun addTrigger(action: Action, trigger: Trigger): Boolean {
        if (action.triggers.contains(trigger))
            return false

        action.triggers.add(trigger)
        return true
    }

    fun removeTrigger(action: Action, trigger: Trigger): Boolean {
        return action.triggers.remove(trigger)
    }

    fun removeAuthor(action: Action): Boolean {
        action.author = "%Unknown%"
        return true
    }
}