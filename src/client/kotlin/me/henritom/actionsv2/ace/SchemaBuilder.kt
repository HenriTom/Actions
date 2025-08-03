package me.henritom.actionsv2.ace

import com.google.gson.GsonBuilder
import me.henritom.actionsv2.axn.task.TaskRegistry
import me.henritom.actionsv2.axn.trigger.TriggerRegistry
import net.fabricmc.loader.api.FabricLoader
import org.apache.logging.log4j.LogManager
import java.nio.file.Paths

private const val schema_version = 1
private val logger = LogManager.getLogger("Actions/SchemaBuilder")
private val dataDir = Paths.get(FabricLoader.getInstance().configDir.toString(), "actionsv2", "data")

object SchemaBuilder {

    private val file = dataDir.resolve("ace_schema.json").toFile()
    private val gson = GsonBuilder().setPrettyPrinting().create()

    fun build(force: Boolean = false): Boolean {
        return if (!file.exists() || getSchemaVersionOfFile() != schema_version || force) {
            file.parentFile.mkdirs()
            file.createNewFile()

            val schema = ACESchema(
                schemaVersion = schema_version,
                triggers = TriggerRegistry.registeredTriggerTypes.toList(),
                tasks = TaskRegistry.registeredTaskTypes.toList()
            )

            file.writeText(gson.toJson(schema))

            true
        } else
            false
    }

    fun getSchemaVersionOfFile(): Int {
        if (file.exists())
            return 0

        return try {
            return gson.fromJson(file.readText(), ACESchema::class.java).schemaVersion
        } catch (e: Exception) {
            logger.error("Failed to read schema version from file: ${file.absolutePath}", e)
            0
        }
    }
}