package de.henritom.actions.triggers.impl

import de.henritom.actions.actions.ActionManager
import de.henritom.actions.triggers.TriggerEnum
import de.henritom.actions.util.MessageUtil

class CommandTrigger {

    fun trigger(message: String) {
        val nameID = message.removePrefix(ActionManager.instance.commandPrefix)

        for (action in ActionManager.instance.actions)
            for (trigger in action.triggers)
                if (trigger.type == TriggerEnum.COMMAND && trigger.value == nameID) {
                    action.call()
                    MessageUtil().printChat("§8» §7Action '${action.name}' called.")
                    return
                }

        if (nameID.isNotEmpty())
            MessageUtil().printChat("§8» §7Command-Trigger '$nameID' not found.")
    }
}