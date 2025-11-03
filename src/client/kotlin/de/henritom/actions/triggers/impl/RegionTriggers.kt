package de.henritom.actions.triggers.impl

import de.henritom.actions.actions.ActionManager
import de.henritom.actions.region.RegionManager
import de.henritom.actions.triggers.TriggerEnum
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents

class RegionTriggers {

    companion object {
        fun register() {
            ClientTickEvents.END_CLIENT_TICK.register { client ->
                for (action in ActionManager.instance.actions) {
                    for (trigger in action.triggers) {
                        when (trigger.type) {
                            TriggerEnum.REGION_ENTER -> {
                                val region = RegionManager.instance.regionByName(trigger.value.toString()) ?: continue
                                val playerPos = client.player?.blockPos ?: continue

                                if (RegionManager.instance.checkForPositionInRegion(playerPos, region))
                                    if (region.playerList.contains(client.player)) continue
                                    else {
                                        region.playerList.add(client.player!!)
                                        action.call(trigger.id)
                                    }
                            }

                            TriggerEnum.REGION_LEAVE -> {
                                val region = RegionManager.instance.regionByName(trigger.value.toString()) ?: continue
                                val playerPos = client.player?.blockPos ?: continue

                                if (!RegionManager.instance.checkForPositionInRegion(playerPos, region))
                                    if (!region.playerList.contains(client.player)) continue
                                    else {
                                        region.playerList.remove(client.player!!)
                                        action.call(trigger.id)
                                    }
                            }

                            else -> { }
                        }
                    }
                }
            }
        }
    }
}