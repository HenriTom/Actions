package de.henritom.actions.triggers.impl

import de.henritom.actions.actions.ActionManager
import de.henritom.actions.triggers.TriggerEnum
import de.henritom.actions.triggers.settings.ReceiveMessageEnum

class ReceiveMessageTrigger {

    fun trigger(message: String) {
        for (action in ActionManager.instance.actions)
            for (trigger in action.triggers) {
                if (trigger.type != TriggerEnum.RECEIVE_MESSAGE)
                    continue

                val split = trigger.value.toString().split("-")
                val triggerType = ReceiveMessageEnum.valueOf(split[0])

                if (triggerType == ReceiveMessageEnum.ANY)
                    action.call()

                if (split.size < 2)
                    return

                val msg = split[1]

                if (triggerType == ReceiveMessageEnum.CONTAINS && message.contains(msg))
                    action.call()

                if (triggerType == ReceiveMessageEnum.CONTAINS_NOT && !message.contains(msg))
                    action.call()

                if (triggerType == ReceiveMessageEnum.STARTS && message.startsWith(msg))
                    action.call()

                if (triggerType == ReceiveMessageEnum.EQUALS && message == msg)
                    action.call()

                if (triggerType == ReceiveMessageEnum.EQUALS_IGNORE_CASE && message.equals(msg, ignoreCase = true))
                    action.call()

            }
    }
}