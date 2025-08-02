package me.henritom.actionsv2.command

import me.henritom.actionsv2.command.impl.AboutCommand
import me.henritom.actionsv2.command.impl.DebugCommand
import me.henritom.actionsv2.command.impl.FileCommand
import me.henritom.actionsv2.command.impl.ListCommand
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback

object ActionsCommand {
    fun register() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                ClientCommandManager.literal("actions")
                    .then(AboutCommand.register())
                    .then(DebugCommand.register())
                    .then(FileCommand.register())
                    .then(ListCommand.register())
            )
        }
    }
}