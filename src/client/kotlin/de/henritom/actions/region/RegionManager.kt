package de.henritom.actions.region

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
        return regions.remove(region)
    }

    fun checkForPositionInRegion(position: BlockPos, region: Region): Boolean {
        if (position.x >= region.minX && position.x <= region.maxX)
            if (position.y >= region.minY && position.y <= region.maxY)
                if (position.z >= region.minZ && position.z <= region.maxZ)
                    return true

        return false
    }

    fun regionByName(name: String): Region? {
        return regions.firstOrNull { it.name == name }
    }
}