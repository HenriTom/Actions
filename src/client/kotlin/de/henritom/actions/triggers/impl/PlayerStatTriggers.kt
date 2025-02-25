package de.henritom.actions.triggers.impl

import de.henritom.actions.actions.ActionManager
import de.henritom.actions.triggers.TriggerEnum
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient

class PlayerStatTriggers {
    companion object {
        private var client = MinecraftClient.getInstance()

        private var lastHealth: Float? = null
        private var lastHunger: Int? = null
        private var lastArmor: Int? = null
        private var lastXP: Float? = null
        private var lastLevel: Int? = null

        fun register() {
            ClientTickEvents.END_CLIENT_TICK.register { client ->
                if (this.client == null)
                    this.client = client

                onTick()

                if (lastHealth != client.player?.health) {
                    onHealthChange()
                    lastHealth = client.player?.health
                }

                if (lastHunger != client.player?.hungerManager?.foodLevel) {
                    onHungerChange()
                    lastHunger = client.player?.hungerManager?.foodLevel
                }

                if (lastArmor != client.player?.armor) {
                    onArmorChange()
                    lastArmor = client.player?.armor
                }

                if (lastXP != client.player?.experienceProgress) {
                    onXPChange()
                    lastXP = client.player?.experienceProgress
                }

                if (lastLevel != client.player?.experienceLevel) {
                    onLevelChange()
                    lastLevel = client.player?.experienceLevel
                }
            }
        }

        private fun onHealthChange() {
            ActionManager.instance.actions.forEach { action ->
                action.triggers.filter { it.type == TriggerEnum.HEALTH_UPDATE }.forEach { _ ->
                    client.player?.health?.let { action.call(it) }
                }
            }
        }

        private fun onHungerChange() {
            ActionManager.instance.actions.forEach { action ->
                action.triggers.filter { it.type == TriggerEnum.HUNGER_UPDATE }.forEach { _ ->
                    client.player?.hungerManager?.foodLevel?.let { action.call(it) }
                }
            }
        }

        private fun onArmorChange() {
            ActionManager.instance.actions.forEach { action ->
                action.triggers.filter { it.type == TriggerEnum.ARMOR_UPDATE }.forEach { _ ->
                    client.player?.armor?.let { action.call(it) }
                }
            }
        }

        private fun onXPChange() {
            ActionManager.instance.actions.forEach { action ->
                action.triggers.filter { it.type == TriggerEnum.XP_UPDATE }.forEach { _ ->
                    client.player?.experienceProgress?.let { action.call(it) }
                }
            }
        }

        private fun onLevelChange() {
            ActionManager.instance.actions.forEach { action ->
                action.triggers.filter { it.type == TriggerEnum.LEVEL_UPDATE }.forEach { _ ->
                    client.player?.experienceLevel?.let { action.call(it) }
                }
            }
        }

        private fun onTick() {
            ActionManager.instance.actions.forEach { action ->
                action.triggers.filter { it.type == TriggerEnum.TICK }.forEach { _ ->
                    action.call(0)
                }
            }
        }
    }
}