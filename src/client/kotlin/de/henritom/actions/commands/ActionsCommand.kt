package de.henritom.actions.commands

import com.mojang.brigadier.Command
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager

class ActionsCommand {
    companion object {
        fun register() {
            CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
                dispatcher.register(
                    CommandManager.literal("actions")
                        .executes {
                            println("run")
                            Command.SINGLE_SUCCESS
                        }
                )
            }
        }
    }
}
