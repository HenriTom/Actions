package de.henritom.actions.triggers.impl

import de.henritom.actions.actions.ActionManager
import de.henritom.actions.triggers.TriggerEnum
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents

class ConnectionTriggers {

    companion object {
        fun register() {
            ClientPlayConnectionEvents.JOIN.register { _, _, _ ->
                for (action in ActionManager.instance.actions)
                    for (trigger in action.triggers)
                        if (trigger.type == TriggerEnum.JOIN)
                            action.call()
            }

            ClientPlayConnectionEvents.DISCONNECT.register { _, _ ->
                for (action in ActionManager.instance.actions)
                    for (trigger in action.triggers)
                        if (trigger.type == TriggerEnum.DISCONNECT)
                            action.call()
            }
        }
    }
}