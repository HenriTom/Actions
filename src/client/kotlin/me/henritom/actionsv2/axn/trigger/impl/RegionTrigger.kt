package me.henritom.actionsv2.axn.trigger.impl

import me.henritom.actionsv2.axn.trigger.TriggerRegistry
import me.henritom.actionsv2.util.RegionUtil
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents

object RegionTrigger {

    fun register() {
        var lastRegions: List<String> = RegionUtil.getCurrentRegions()

        ClientTickEvents.END_CLIENT_TICK.register { client ->
            if (lastRegions != RegionUtil.getCurrentRegions()) {
                val enteredRegions = RegionUtil.getCurrentRegions().filter { !lastRegions.contains(it) }
                val leftRegions = lastRegions.filter { !RegionUtil.getCurrentRegions().contains(it) }

                for (region in enteredRegions)
                    trigger(true, region)

                for (region in leftRegions)
                    trigger(false, region)
            }

            lastRegions = RegionUtil.getCurrentRegions()
        }
    }

    fun trigger(enter: Boolean, region: String) {
        TriggerRegistry.getTriggersByType("region_update").forEach { trigger ->
            val triggerRegion = trigger.data["region"]?.toString() ?: "no_region"
            val triggerType = trigger.data["type"]?.toString()?.lowercase() ?: "any"

            if (triggerRegion != region)
                return@forEach

            when (triggerType) {
                "any" -> trigger.trigger(region)
                "enter" if enter -> trigger.trigger(region)
                "leave" if !enter -> trigger.trigger(region)
            }
        }
    }
}