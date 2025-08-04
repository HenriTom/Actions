package me.henritom.actionsv2.regions

import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.loader.api.FabricLoader
import org.apache.logging.log4j.LogManager
import java.lang.reflect.Type
import java.nio.file.Paths

object RegionManager {

    private val logger = LogManager.getLogger("Actions/RegionManager")
    private val dataDir = Paths.get(FabricLoader.getInstance().configDir.toString(), "actionsv2", "data")
    private val file = dataDir.resolve("regions.json").toFile()
    private val gson = GsonBuilder().setPrettyPrinting().create()

    private val regions = mutableMapOf<String, Region>()
    val regs: Map<String, Region> get() = regions

    fun init() {
        loadRegions()

        ClientLifecycleEvents.CLIENT_STOPPING.register {
            saveRegions()
        }
    }

    fun loadRegions() {
        try {
            if (file.exists())
                file.readText().let { content ->
                    val type: Type = object : TypeToken<Map<String, Region>>() {}.type
                    val loadedRegions: Map<String, Region> = gson.fromJson(content, type)

                    loadedRegions.forEach { (id, region) ->
                        region.id = id
                    }

                    regions.putAll(loadedRegions)
                }
            logger.info("Loaded ${regions.size} regions.")
        } catch (e: Exception) {
            e.printStackTrace()
            logger.error("Failed to load regions: ${e.message}")
        }
    }

    fun saveRegions() {
        try {
            val file = dataDir.resolve("regions.json").toFile()
            file.parentFile.mkdirs()
            file.writeText(gson.toJson(regions))

            logger.info("Saved ${regions.size} regions.")
        } catch (e: Exception) {
            e.printStackTrace()
            logger.error("Failed to save regions: ${e.message}")
        }
    }

    fun registerRegion(region: Region) {
        regions[region.id] = region
    }

    fun getRegion(id: String): Region? {
        return regions[id]
    }

    fun removeRegion(region: Region) {
        regions.remove(region.id)
    }
}