package de.henritom.actions.actions

import de.henritom.actions.config.ConfigManager
import de.henritom.actions.motion.MoveEnum
import de.henritom.actions.motion.MoveManager
import de.henritom.actions.tasks.TaskEnum
import de.henritom.actions.triggers.TriggerEnum
import de.henritom.actions.util.MessageUtil
import net.minecraft.client.MinecraftClient
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

    // 1: Success | 2: Name already used | 3: Name must start with a letter | 4: Name must be at least 3 characters long
    fun createAction(name: String, addCallTrigger: Boolean = true): Int  {
        if (name.length < 3)
            return 4

        if (!name.first().isLetter())
            return 3

        if (actions.any { it.name == name })
            return 2

        object : Action(name) {
            override fun call() {
                Thread {
                    for (task in tasks) {
                        when (task.type) {
                            TaskEnum.SAY -> MessageUtil().sayChat(task.value.toString())
                            TaskEnum.PRINT -> MessageUtil().printChat(task.value.toString())
                            TaskEnum.COMMAND -> MessageUtil().sendCommand(task.value.toString())
                            TaskEnum.CONSOLE -> println(task.value)
                            TaskEnum.WAIT -> sleep(task.value.toString().toLongOrNull() ?: 0)
                            TaskEnum.MOVE -> MoveManager().setMovement(MoveEnum.valueOf(task.value.toString()))
                        }
                    }
                }.start()
            }
        }.let {
            if (addCallTrigger)
                ActionEditManager.instance.addTrigger(it, TriggerEnum.CALL)

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
}