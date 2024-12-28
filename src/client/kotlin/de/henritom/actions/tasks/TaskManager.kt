package de.henritom.actions.tasks

import de.henritom.actions.actions.Action

class TaskManager {

    companion object {
        val instance = TaskManager()
    }

    fun getNextAvailableID(action: Action): Int {
        for (id in 1..Int.MAX_VALUE)
            if (action.tasks.none { it.id == id })
                return id

        return -1
    }
}