package de.henritom.actions.commands

import de.henritom.actions.commands.impl.action.ActionCommand
import de.henritom.actions.commands.impl.file.FileCommand
import de.henritom.actions.commands.impl.list.ListCommand
import de.henritom.actions.commands.impl.prefix.PrefixCommand
import de.henritom.actions.commands.impl.version.VersionCommand
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback

class ActionsCommand {
    companion object {
        fun register() {
            ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
                dispatcher.register(
                    ClientCommandManager.literal("actions")
                        .then(ActionCommand.register())
                        .then(FileCommand.register())
                        .then(ListCommand.register())
                        .then(PrefixCommand.register())
                        .then(VersionCommand.register())
                )
            }
        }
    }
}