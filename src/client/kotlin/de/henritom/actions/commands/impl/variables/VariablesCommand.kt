package de.henritom.actions.commands.impl.variables

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.commands.impl.variables.add.VariablesAddCommand
import de.henritom.actions.commands.impl.variables.globallist.VariablesGlobalListCommand
import de.henritom.actions.commands.impl.variables.list.VariablesListCommand
import de.henritom.actions.commands.impl.variables.remove.VariablesRemoveCommand
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object VariablesCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("variables")
            .then(VariablesAddCommand.register())
            .then(VariablesGlobalListCommand.register())
            .then(VariablesListCommand.register())
            .then(VariablesRemoveCommand.register())
    }
}