package de.henritom.actions.triggers.impl

import de.henritom.actions.actions.ActionManager
import de.henritom.actions.triggers.TriggerEnum
import de.henritom.actions.util.KeyBindUtil
import net.minecraft.client.MinecraftClient

class KeyTrigger {

    fun trigger(key: Int) {
        if (MinecraftClient.getInstance().currentScreen != null)
            return

        for (action in ActionManager.instance.actions)
            for (trigger in action.triggers)
                if (trigger.type == TriggerEnum.KEYBIND)
                    if (trigger.value.toString().toIntOrNull() == key || KeyBindUtil().getKeyCodeByString(trigger.value.toString()) == key)
                        action.call()
    }
}