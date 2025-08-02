package me.henritom.actionsv2.variables

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.loader.api.FabricLoader
import org.apache.logging.log4j.LogManager
import java.nio.file.Paths

object LocalVariableStorage {

    private val logger = LogManager.getLogger("Actions/LVStorage")
    private val dataDir = Paths.get(FabricLoader.getInstance().configDir.toString(), "actionsv2", "data")
    private val variables = mutableMapOf<String, JsonPrimitive>()
    val vars: Map<String, JsonPrimitive> get() = variables

    fun init() {
        loadVariables()

        ClientLifecycleEvents.CLIENT_STOPPING.register {
            saveVariables()
        }
    }

    private fun loadVariables() {
        try {
            val file = dataDir.resolve("local_variables.json").toFile()
            if (file.exists())
                file.readText().let { content ->
                    val loadedVariables = Json.decodeFromString<Map<String, JsonPrimitive>>(content)
                    variables.putAll(loadedVariables)
                }
        } catch (e: Exception) {
            e.printStackTrace()
            logger.error("Failed to load local variables: ${e.message}")
        }
    }

    private fun saveVariables() {
        try {
            val file = dataDir.resolve("local_variables.json").toFile()
            file.parentFile.mkdirs()
            file.writeText(Json { prettyPrint = true }.encodeToString(variables))
        } catch (e: Exception) {
            e.printStackTrace()
            logger.error("Failed to save local variables: ${e.message}")
        }
    }

    fun setVariable(name: String, value: JsonPrimitive) {
        variables[name] = value
    }

    fun removeVariable(name: String) {
        variables.remove(name)
    }

    fun getVariable(name: String): String? {
        return variables[name]?.content
    }
}