package de.henritom.actions.commands.impl.action

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.commands.impl.action.call.CallCommand
import de.henritom.actions.commands.impl.action.clone.CloneCommand
import de.henritom.actions.commands.impl.action.create.CreateCommand
import de.henritom.actions.commands.impl.action.delete.DeleteCommand
import de.henritom.actions.commands.impl.action.disable.DisableCommand
import de.henritom.actions.commands.impl.action.edit.EditCommand
import de.henritom.actions.commands.impl.action.enable.EnableCommand
import de.henritom.actions.commands.impl.action.info.InfoCommand
import de.henritom.actions.commands.impl.action.rename.RenameCommand
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object ActionCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("action")
            .then(CallCommand.register())
            .then(CloneCommand.register())
            .then(CreateCommand.register())
            .then(DeleteCommand.register())
            .then(DisableCommand.register())
            .then(EditCommand.register())
            .then(EnableCommand.register())
            .then(InfoCommand.register())
            .then(RenameCommand.register())
    }
}