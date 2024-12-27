package de.henritom.actions.event.impl

import de.henritom.actions.actions.ActionManager
import de.henritom.actions.trigger.impl.CommandTrigger
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents

class ChatEvent {
    companion object {
        fun register() {
            ClientSendMessageEvents.ALLOW_CHAT.register(ClientSendMessageEvents.AllowChat { message ->
                if (ActionManager.instance.commandPrefix.isEmpty())
                    return@AllowChat true

                if (message.startsWith(ActionManager.instance.commandPrefix)) {
                    CommandTrigger().trigger(message)
                    false
                } else
                    true
            })
        }
    }
}