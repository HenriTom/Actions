package de.henritom.actions.triggers.impl

import de.henritom.actions.actions.ActionManager
import de.henritom.actions.triggers.TriggerEnum
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.world.biome.Biome

class BiomeTriggers {

    companion object {
        private var lastBiomeEnter: RegistryEntry<Biome>? = null
        private var lastBiomeLeave: RegistryEntry<Biome>? = null

        fun register() {
            ClientTickEvents.END_CLIENT_TICK.register { client ->
                for (action in ActionManager.instance.actions.toList()) {
                    for (trigger in action.triggers) {
                        when (trigger.type) {
                            TriggerEnum.BIOME_ENTER -> {
                                val biome = client.world?.getBiome(client.player?.blockPos)

                                if (biome != lastBiomeEnter && biome != null)
                                    if (biome.key.get().value.toString().contains(trigger.value.toString()))
                                        action.call(trigger.id)

                                lastBiomeEnter = biome
                            }

                            TriggerEnum.BIOME_LEAVE -> {
                                val biome = client.world?.getBiome(client.player?.blockPos)

                                if (biome != lastBiomeLeave && lastBiomeLeave != null)
                                    if (lastBiomeLeave!!.key.get().value.toString().contains(trigger.value.toString()))
                                        action.call(trigger.id)

                                lastBiomeLeave = biome
                            }

                            else -> { }
                        }
                    }
                }
            }
        }
    }
}