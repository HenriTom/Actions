package de.henritom.actions.actions

import de.henritom.actions.config.ConfigManager
import de.henritom.actions.tasks.Task
import de.henritom.actions.tasks.TaskEnum
import de.henritom.actions.triggers.Trigger
import de.henritom.actions.triggers.TriggerEnum
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import java.io.File
import java.nio.file.Files

class ActionEditManager {
    companion object {
        val instance = ActionEditManager()
    }

    fun addDirectTrigger(action: Action, trigger: Trigger): Boolean {
        if (trigger.type == TriggerEnum.CALL)
            if (action.triggers.any { it.type == TriggerEnum.CALL })
                return false

        return action.triggers.add(trigger)
    }

    // 1 = success, 2 = can't add multiple call triggers
    fun addTrigger(action: Action, trigger: TriggerEnum): Int {
        if (trigger == TriggerEnum.CALL)
            if (action.triggers.any { it.type == TriggerEnum.CALL })
                return 2

        action.triggers.add(Trigger(action, trigger))
        return 1
    }

    fun removeTrigger(action: Action, trigger: Trigger): Boolean {
        return action.triggers.remove(trigger)
    }

    fun addDirectTask(action: Action, task: Task): Boolean {
        return action.tasks.add(task)
    }

    fun addTask(action: Action, task: TaskEnum): Boolean {
        return action.tasks.add(Task(action, task))
    }

    fun removeTask(action: Action, task: Task): Boolean {
        return action.tasks.remove(task)
    }

    fun removeAuthor(action: Action): Boolean {
        action.author = "%Unknown%"
        return true
    }

    fun disableAction(action: Action): Boolean {
        ConfigManager().deleteAction(action)
        ConfigManager().saveAction(action, false)
        ActionManager().actions.remove(action)

        return true
    }

    fun enableAction(file: File): Boolean {
        if (!file.exists())
            return false

        Files.copy(file.toPath(), file.parentFile.resolve(file.nameWithoutExtension + ".json").outputStream())
        file.delete()

        return true
    }

    fun renameAction(action: Action, newName: String): Action? {
        if (action.name == newName)
            return null

        Files.copy(action.file!!.toPath(), action.file!!.parentFile.resolve("$newName.json").outputStream())
        action.file!!.parentFile.resolve("$newName.json").writeText(Json.encodeToString(JsonObject(Json.parseToJsonElement(action.file!!.readText()).jsonObject.toMutableMap().apply { this["name"] = JsonPrimitive(newName) })))
        ActionManager.instance.deleteAction(action.name)

        ConfigManager().reloadActions()

        return ActionManager.instance.getActionByNameID(newName)
    }
}