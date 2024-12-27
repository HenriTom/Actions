package de.henritom.actions.triggers.impl

import de.henritom.actions.actions.ActionManager
import de.henritom.actions.triggers.TriggerEnum
import net.minecraft.client.MinecraftClient

class KeyTrigger {

    fun trigger(key: Int) {
        if (MinecraftClient.getInstance().currentScreen != null)
            return

        for (action in ActionManager.instance.actions)
            for (trigger in action.triggers)
                if (trigger.type == TriggerEnum.KEYBIND && trigger.value.toString().toIntOrNull() == key)
                    action.call()
    }
}