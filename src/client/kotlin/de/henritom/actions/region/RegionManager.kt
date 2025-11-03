package de.henritom.actions.region

import de.henritom.actions.config.ConfigManager
import net.minecraft.client.MinecraftClient
import net.minecraft.util.math.BlockPos

class RegionManager {

    companion object {
        val instance = RegionManager()
    }

    val regions = mutableListOf<Region>()

    fun addRegion(region: Region) {
        if (regions.none { it.name == region.name })
            regions.add(region)
    }

    fun removeRegion(region: Region): Boolean {
        ConfigManager().removeRegion(region.name)
        return regions.remove(region)
    }

    fun checkForPositionInRegion(position: BlockPos, region: Region): Boolean {
        if (position.x >= region.minX && position.x <= region.maxX)
            if (position.y >= region.minY && position.y <= region.maxY)
                if (position.z >= region.minZ && position.z <= region.maxZ)
                    return true

        return false
    }

    fun regionsThePlayerIsIn(): MutableList<String> {
        val list = mutableListOf<String>()

        for (region in regions)
            if (region.playerList.contains(MinecraftClient.getInstance().player))
                list.add(region.name)

        return list
    }

    fun regionByName(name: String): Region? {
        return regions.firstOrNull { it.name == name }
    }
}