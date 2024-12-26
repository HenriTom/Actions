package de.henritom.actions.actions

import de.henritom.actions.trigger.Trigger
import net.minecraft.client.MinecraftClient

class ActionManager {
    companion object {
        val instance = ActionManager()
    }

    val actions = mutableListOf<Action>()

    // 1: Success | 2: Action not found | 3: Action is not callable
    fun callAction(nameID: String): Int {
        getActionByNameID(nameID)?.let { action ->
            if (action.triggers.contains(Trigger.CALL)) {
                action.call()
                return 1
            } else
                return 3
        }

        return 2
    }

    // 1: Success | 2: Name already used | 3: Name must start with a letter | 4: Name must be at least 3 characters long
    fun createAction(name: String): Int  {
        if (name.length < 3)
            return 4

        if (!name.first().isLetter())
            return 3

        if (actions.any { it.name == name })
            return 2

        object : Action(name) {
            override fun call() {
                println("[AM] Action $name called.")
            }
        }.let {
            actions.add(it)
            it.author = MinecraftClient.getInstance().player?.name?.literalString ?: "%Unknown%"
        }

        return 1
    }

    fun deleteAction(nameID: String): Boolean  {
        getActionByNameID(nameID)?.let {
            actions.remove(it)
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