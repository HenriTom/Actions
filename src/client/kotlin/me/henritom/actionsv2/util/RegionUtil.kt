package me.henritom.actionsv2.util

import me.henritom.actionsv2.regions.Region
import me.henritom.actionsv2.regions.RegionManager
import net.minecraft.client.MinecraftClient

object RegionUtil {

    fun getCurrentRegions(): List<String> {
        val list = ArrayList<String>()

        for (region in RegionManager.regs)
            if (playerIsInRegion(region.value))
                list.add(region.key)

        if (list.isEmpty())
            list.add("no_region")

        return list
    }

    fun playerIsInRegion(region: Region): Boolean {
        val client = MinecraftClient.getInstance()
        val player = client.player ?: return false
        val pos = player.pos

        if (pos.x >= region.x1 && pos.x <= region.x2)
            if (pos.y >= region.y1 && pos.y <= region.y2)
                if (pos.z >= region.z1 && pos.z <= region.z2)
                    return true

        return false
    }
}