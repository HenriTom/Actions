package de.henritom.actions.triggers

import de.henritom.actions.actions.Action

class TriggerManager {

    companion object {
        val instance = TriggerManager()
    }

    fun getNextAvailableID(action: Action): Int {
        for (id in 1..Int.MAX_VALUE)
            if (action.triggers.none { it.id == id })
                return id

        return -1
    }
}