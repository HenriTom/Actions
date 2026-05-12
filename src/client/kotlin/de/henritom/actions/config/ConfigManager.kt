package de.henritom.actions.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import de.henritom.actions.ActionsClient
import de.henritom.actions.actions.Action
import de.henritom.actions.actions.ActionEditManager
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.region.Region
import de.henritom.actions.region.RegionManager
import de.henritom.actions.tasks.Task
import de.henritom.actions.tasks.TaskEnum
import de.henritom.actions.triggers.Trigger
import de.henritom.actions.triggers.TriggerEnum
import de.henritom.actions.triggers.TriggerManager
import de.henritom.actions.ui.impl.SettingsScreen
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.math.Vec3d
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

class ConfigManager {

    private val logger: Logger = LoggerFactory.getLogger("actions_legacy")

    fun saveConfig() {
        val gson = GsonBuilder().setPrettyPrinting().create()

        val configDir = FabricLoader.getInstance().configDir.toFile()
        val configFile = configDir.resolve("actions/config.json")

        if (!configFile.parentFile.exists())
            configFile.parentFile.mkdirs()

        if (!configFile.exists())
            configFile.createNewFile()

        val data = mapOf(
            "command_prefix" to ActionManager.instance.commandPrefix,
            "options_button" to SettingsScreen.addOptionsScreenButton
        )

        configFile.writeText(gson.toJson(data))
    }

    fun loadConfig() {
        val configDir = FabricLoader.getInstance().configDir.toFile()
        val configFile = configDir.resolve("actions/config.json")

        if (!configFile.exists() || configFile.isDirectory)
            return

        val data = Gson().fromJson(configFile.readText(), Map::class.java) as Map<*, *>
        ActionManager.instance.commandPrefix = data["command_prefix"] as? String ?: ""
        SettingsScreen.addOptionsScreenButton = data["options_button"] as? Boolean ?: true
    }

    fun saveRegions() {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val configDir = FabricLoader.getInstance().configDir.toFile()

        for (region in RegionManager.instance.regions) {
            val configFile = configDir.resolve("actions/regions/${region.name}.json")

            if (!configFile.parentFile.exists())
                configFile.parentFile.mkdirs()

            if (!configFile.exists())
                configFile.createNewFile()

            val data = mapOf(
                "name" to region.name,
                "x1" to region.pos1.x,
                "y1" to region.pos1.y,
                "z1" to region.pos1.z,
                "x2" to region.pos2.x,
                "y2" to region.pos2.y,
                "z2" to region.pos2.z
            )

            configFile.writeText(gson.toJson(data))
        }
    }

    fun loadRegions() {
        val configDir = FabricLoader.getInstance().configDir.toFile().resolve("actions/regions")

        if (configDir.listFiles() == null)
            return

        for (file in configDir.listFiles()!!) {
            if (!file.exists() || file.isDirectory)
                return

            val data = Gson().fromJson(file.readText(), Map::class.java) as Map<*, *>
            val name = data["name"] as String
            val x1 = data["x1"] as? Double ?: 0.0
            val y1 = data["y1"] as? Double ?: 0.0
            val z1 = data["z1"] as? Double ?: 0.0
            val x2 = data["x2"] as? Double ?: 0.0
            val y2 = data["y2"] as? Double ?: 0.0
            val z2 = data["z2"] as? Double ?: 0.0

            RegionManager.instance.addRegion(Region(name, Vec3d(x1, y1, z1), Vec3d(x2, y2, z2)))
        }
    }

    fun removeRegion(name: String): Boolean {
        return FabricLoader.getInstance().configDir.toFile().resolve("actions/regions/$name.json").delete()
    }

    fun saveAction(action: Action, enabled: Boolean = true): File? {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val configDir = FabricLoader.getInstance().configDir.toFile()
        val actionFile = configDir.resolve("actions/actions/${action.name}." + if (enabled) "json" else "disabled")

        if (!actionFile.parentFile.exists())
            actionFile.parentFile.mkdirs()

        if (!actionFile.exists())
            actionFile.createNewFile()

        val data = mapOf(
            "name" to action.name,
            "preferred_id" to action.id,
            "author" to action.author,
            "triggers" to action.triggers.map { trigger ->
                mapOf(
                    "id" to trigger.id,
                    "type" to trigger.type,
                    "value" to trigger.value
                )
            },
            "tasks" to action.tasks.map { task ->
                mapOf(
                    "id" to task.id,
                    "type" to task.type,
                    "value" to task.value
                )
            }
        )

        try {
            actionFile.writeText(gson.toJson(data))
            return actionFile
        } catch (e: Exception) {
            logger.error("Failed to save action ${action.name}!", e)
        }

        return null
    }

    fun saveAllActions() {
        logger.info("Trying to save all actions...")

        for (action in ActionManager.instance.actions.toList())
            saveAction(action)

        logger.info("Saved all actions!")
    }

    fun copyFileToFolder(file: File, fileName: String) {
        val configDir = FabricLoader.getInstance().configDir.toFile()
        val actionsDir = configDir.resolve("actions/actions")

        if (!actionsDir.exists())
            actionsDir.mkdirs()

        file.copyTo(actionsDir.resolve(fileName), true)
    }

    fun reloadActions() {
        ActionManager.instance.actions.clear()
        loadActions()
    }

    fun loadActions() {
        logger.info("Trying to load actions...")

        val configDir = FabricLoader.getInstance().configDir.toFile()
        val actionsDir = configDir.resolve("actions/actions")

        if (!actionsDir.exists() || !actionsDir.isDirectory) {
            logger.info("No actions directory found, skipping load.")
            return
        }

        val gson = Gson()
        val files = actionsDir.listFiles() ?: emptyArray()

        for (file in files) {
            if (!file.isFile || !file.name.endsWith(".json"))
                continue

            try {
                val data = gson.fromJson(file.readText(), Map::class.java) as Map<*, *>

                val name = data["name"] as? String ?: run {
                    return@run null
                } ?: continue

                val preferredID = (data["preferred_id"] as? Double)?.toInt() ?: -1
                val author = data["author"] as? String ?: "Unknown"

                if (ActionManager.instance.createAction(name, false, file) == 1) {
                    val action = ActionManager.instance.getActionByNameID(name)

                    if (action == null) {
                        logger.error("Failed to load action $name!")
                        continue
                    }

                    if (action.id != preferredID)
                        action.id = if (ActionManager.instance.actions.none { it.id == preferredID })
                            preferredID
                        else
                            ActionManager.instance.getNextAvailableID()

                    action.author = author

                    val triggers = if (data["triggers"] is List<*>)
                        (data["triggers"] as List<*>)
                    else
                        emptyList<Map<*, *>>()

                    for (triggerIt in triggers) {
                        val triggerData = triggerIt as? Map<*, *> ?: continue
                        val triggerID = (triggerData["id"] as? Double)?.toInt() ?: -1
                        val triggerType = triggerData["type"] as? String ?: continue
                        val triggerValue = triggerData["value"] as? String ?: 0

                        val trigger = Trigger(action, TriggerEnum.valueOf(triggerType), triggerValue)

                        if (trigger.id != triggerID)
                            trigger.id = if (action.triggers.none { it.id == triggerID })
                                triggerID
                            else
                                TriggerManager.instance.getNextAvailableID(action)

                        ActionEditManager.instance.addDirectTrigger(action, trigger)
                    }

                    val tasks = if (data["tasks"] is List<*>)
                        (data["tasks"] as List<*>)
                    else
                        emptyList<Map<*, *>>()

                    for (taskIt in tasks) {
                        val taskData = taskIt as? Map<*, *> ?: continue
                        val taskID = (taskData["id"] as? Double)?.toInt() ?: -1
                        val taskType = taskData["type"] as? String ?: continue
                        val taskValue = taskData["value"] as? String ?: ""

                        val task = Task(action, TaskEnum.valueOf(taskType), taskValue)

                        if (task.id != taskID)
                            task.id = if (action.tasks.none { it.id == taskID })
                                taskID
                            else
                                ActionManager.instance.getNextAvailableID()

                        ActionEditManager.instance.addDirectTask(action, task)
                    }
                } else
                    logger.error("Failed to create action $name!")
            } catch (e: Exception) {
                logger.error("Error loading file ${file.name}: ${e.message}", e)
            }
        }

        logger.info("Loaded actions!")
    }

    fun deleteAction(action: Action) {
        val configDir = FabricLoader.getInstance().configDir.toFile()
        val actionFile = configDir.resolve("actions/actions/${action.name}.json")

        if (actionFile.exists())
            actionFile.delete()
    }

    fun getDisabledActions(): List<File> {
        val configDir = FabricLoader.getInstance().configDir.toFile()
        val actionsDir = configDir.resolve("actions/actions")

        if (!actionsDir.exists() || !actionsDir.isDirectory)
            return emptyList()

        return actionsDir.listFiles { _, name -> name.endsWith(".disabled") }?.toList() ?: emptyList()
    }

    fun reloadRegions() {
        RegionManager.instance.regions.clear()
        loadRegions()
    }

    fun saveLocalVariables() {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val configDir = FabricLoader.getInstance().configDir.toFile()
        val localVariablesFile = configDir.resolve("actions/local_variables.json")

        if (!localVariablesFile.parentFile.exists())
            localVariablesFile.parentFile.mkdirs()

        if (!localVariablesFile.exists())
            localVariablesFile.createNewFile()

        val data = mapOf(
            "variables" to ActionsClient.localVariableStorage.variables
        )

        localVariablesFile.writeText(gson.toJson(data))
    }

    fun loadLocalVariables() {
        val configDir = FabricLoader.getInstance().configDir.toFile()
        val localVariablesFile = configDir.resolve("actions/local_variables.json")

        if (!localVariablesFile.exists() || localVariablesFile.isDirectory)
            return

        val data = Gson().fromJson(localVariablesFile.readText(), Map::class.java) as Map<*, *>
        val variables = data["variables"] as? Map<String, Any> ?: emptyMap()

        ActionsClient.localVariableStorage.variables.clear()
        ActionsClient.localVariableStorage.variables.putAll(variables)
    }
}