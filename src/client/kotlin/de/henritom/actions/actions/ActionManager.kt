package de.henritom.actions.actions

import de.henritom.actions.ActionsClient
import de.henritom.actions.conditions.ConditionHelper
import de.henritom.actions.config.ConfigManager
import de.henritom.actions.motion.MoveEnum
import de.henritom.actions.motion.MoveManager
import de.henritom.actions.scheduler.ActionScheduler
import de.henritom.actions.tasks.TaskEnum
import de.henritom.actions.triggers.TriggerEnum
import de.henritom.actions.util.MessageUtil
import de.henritom.actions.util.PlayerUtil
import de.henritom.actions.util.SoundUtil
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import java.io.File
import java.lang.Thread.sleep

class ActionManager {
    companion object {
        val instance = ActionManager()
    }

    val actions = mutableListOf<Action>()
    var commandPrefix = ""

    // 1: Success | 2: Action not found | 3: Action is not callable
    fun callAction(nameID: String): Int {
        getActionByNameID(nameID)?.let { action ->
            for (trigger in action.triggers)
                if (trigger.type == TriggerEnum.CALL) {
                    action.call()
                    return 1
                }

            return 3
        }

        return 2
    }

    // 1: Success | 2: Name already used | 3: Name must start with a letter | 4: Name must be between 3 and 16 characters
    fun createAction(name: String, addCallTrigger: Boolean = true, file: File? = null): Int  {
        if (name.length < 3 || name.length > 16)
            return 4

        if (!name.first().isLetter())
            return 3

        if (actions.any { it.name == name })
            return 2

        object : Action(name) {
            override fun call(vararg callArgs: Any) {
                super.call(*callArgs)

                Thread {
                    val actionScheduler = ActionScheduler(this).start()

                    if (tasks.isEmpty())
                        actionScheduler.end()

                    val messageUtil = MessageUtil(this)

                    var index = 0
                    while (index < tasks.size) {
                        val task = tasks[index]
                        actionScheduler.currentTask = index

                        if (actionScheduler.cancelled)
                            return@Thread

                        when (task.type) {
                            TaskEnum.COMMAND -> messageUtil.sendCommandByAction(task.value.toString())
                            TaskEnum.COMMENT -> {}
                            TaskEnum.CONSOLE -> messageUtil.printConsoleByAction(task.value.toString(), actionScheduler)
                            TaskEnum.END -> actionScheduler.end()
                            TaskEnum.IF -> ConditionHelper.checkIf(task.value.toString()).let { if (!it) index++ }
                            TaskEnum.JUMP_TASK -> index = (task.value.toString().toIntOrNull()?.takeIf { it in tasks.indices }?.minus(1) ?: index) - 1
                            TaskEnum.MINE -> MoveManager().setMining(task.value.toString().toBoolean())
                            TaskEnum.MOVE -> MoveManager().setMovement(MoveEnum.valueOf(task.value.toString()))
                            TaskEnum.PRINT -> messageUtil.printChatByAction(task.value.toString())
                            TaskEnum.SAY -> messageUtil.sayChatByAction(task.value.toString())
                            TaskEnum.SET_SLOT -> PlayerUtil.setHotbarSlot(task.value.toString().toIntOrNull() ?: 10)
                            TaskEnum.SOUND -> SoundUtil.playSound(task.value.toString())
                            TaskEnum.USE -> MoveManager().setUse(task.value.toString().toBoolean())
                            TaskEnum.VARIABLE_SET -> ActionsClient.localVariableStorage.setVariable(task.value.toString().split(" ")[0].ifEmpty { "null" }, if (task.value.toString().split(" ").size < 2) "null" else task.value.toString().split(" ")[1])
                            TaskEnum.VARIABLE_REMOVE -> ActionsClient.localVariableStorage.removeVariable(task.value.toString())
                            TaskEnum.WAIT -> sleep(task.value.toString().toLongOrNull() ?: 0)
                        }

                        index++

                        if (index >= tasks.size)
                            actionScheduler.end()
                    }
                }.start()
            }
        }.let {
            if (addCallTrigger)
                ActionEditManager.instance.addTrigger(it, TriggerEnum.CALL)

            if (file == null)
                it.file = ConfigManager().saveAction(it)
            else
                it.file = file

            actions.add(it)
            it.author = MinecraftClient.getInstance().player?.name?.literalString ?: "%Unknown%"
        }

        return 1
    }

    fun deleteAction(nameID: String): Boolean  {
        getActionByNameID(nameID)?.let {
            actions.remove(it)

            ConfigManager().deleteAction(it)
            return true
        }

        return false
    }

    fun getActionByNameID(nameID: String): Action? {
        return nameID.toIntOrNull()?.let { id ->
            actions.find { it.id == id }
        } ?: actions.find { it.name == nameID }
    }

    fun getNextAvailableID(): Int {
        for (id in 1..Int.MAX_VALUE)
            if (actions.none { it.id == id })
                return id

        return -1
    }

    fun getAvailableTriggersForAction(action: Action): List<TriggerEnum> {
        return TriggerEnum.entries.filter { trigger -> action.triggers.none { it.type == trigger && trigger in listOf(TriggerEnum.CALL, TriggerEnum.JOIN, TriggerEnum.DISCONNECT, TriggerEnum.RESPAWN) } }
    }

    fun getDisabledActions(): List<File> {
        val list = mutableListOf<File>()

        for (file in FabricLoader.getInstance().configDir.resolve("actions").resolve("actions").toFile().listFiles() ?: emptyArray())
            if (file.name.endsWith(".disabled"))
                list.add(file)

        return list
    }
}