package de.henritom.actions.actions

import de.henritom.actions.tasks.Task
import de.henritom.actions.tasks.TaskEnum
import de.henritom.actions.triggers.Trigger
import de.henritom.actions.triggers.TriggerEnum

class ActionEditManager {
    companion object {
        val instance = ActionEditManager()
    }

    // 1 = success, 2 = can't add multiple call triggers
    fun addTrigger(action: Action, trigger: TriggerEnum): Int {
        if (trigger == TriggerEnum.CALL)
            if (action.triggers.any { it.type == TriggerEnum.CALL })
                return 2

        action.triggers.add(Trigger(action, trigger))
        return 1
    }

    fun removeTrigger(action: Action, trigger: Trigger): Boolean {
        return action.triggers.remove(trigger)
    }

    fun addTask(action: Action, task: TaskEnum): Boolean {
        return action.tasks.add(Task(action, task))
    }

    fun removeTask(action: Action, task: Task): Boolean {
        return action.tasks.remove(task)
    }

    fun removeAuthor(action: Action): Boolean {
        action.author = "%Unknown%"
        return true
    }
}