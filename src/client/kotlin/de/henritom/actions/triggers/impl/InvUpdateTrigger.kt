package de.henritom.actions.triggers.impl

import de.henritom.actions.actions.ActionManager
import de.henritom.actions.triggers.TriggerEnum
import de.henritom.actions.triggers.settings.InvUpdateEnum
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.item.Item

class InvUpdateTrigger {

    companion object {
        private var lastInv: Map<Item, Int>? = null

        fun reg() {
            ClientTickEvents.END_CLIENT_TICK.register { client ->
                val inv = (client.player ?: return@register).inventory
                    .groupingBy { it.item }
                    .fold(0) { sum, stack -> sum + stack.count }

                if (lastInv != null && lastInv != inv)
                    trigger(inv)

                lastInv = inv
            }
        }

        private fun trigger(inv: Map<Item, Int>) {
            for (action in ActionManager.instance.actions) {
                for (trigger in action.triggers) {
                    if (trigger.type == TriggerEnum.INV_UPDATE) {
                        val triggerStr = trigger.value.toString()

                        if (triggerStr.split("-").isEmpty())
                            continue

                        val triggerType = try {
                            InvUpdateEnum.valueOf(triggerStr.split("-")[0])
                        } catch (e: IllegalArgumentException) {
                            continue
                        }

                        when (triggerType) {
                            InvUpdateEnum.ANY -> action.call()
                            InvUpdateEnum.CONTAINS_ANY -> if (triggerStr.split("-").size > 1 && inv.keys.any { it.translationKey.contains(triggerStr.split("-")[1].lowercase()) }) action.call()
                            InvUpdateEnum.CONTAINS_EXACT -> if (triggerStr.split("-").size > 2 && inv.keys.any { it.translationKey.contains(triggerStr.split("-")[1].lowercase()) && inv[it] == triggerStr.split("-")[2].toInt() }) action.call()
                            InvUpdateEnum.CONTAINS_LESS -> if (triggerStr.split("-").size > 2 && inv.keys.any { it.translationKey.contains(triggerStr.split("-")[1].lowercase()) && inv[it]!! < triggerStr.split("-")[2].toInt() }) action.call()
                            InvUpdateEnum.CONTAINS_MORE -> if (triggerStr.split("-").size > 2 && inv.keys.any { it.translationKey.contains(triggerStr.split("-")[1].lowercase()) && inv[it]!! > triggerStr.split("-")[2].toInt() }) action.call()
                        }
                    }
                }
            }
        }
    }
}