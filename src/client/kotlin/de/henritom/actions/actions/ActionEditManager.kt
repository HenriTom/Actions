package de.henritom.actions.actions

import de.henritom.actions.trigger.Trigger
import de.henritom.actions.trigger.TriggerEnum

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

    fun removeAuthor(action: Action): Boolean {
        action.author = "%Unknown%"
        return true
    }
}