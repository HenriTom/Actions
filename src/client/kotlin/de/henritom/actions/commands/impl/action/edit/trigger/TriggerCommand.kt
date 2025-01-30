package de.henritom.actions.commands.impl.action.edit.trigger

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.commands.impl.action.edit.trigger.add.TriggerAddCommand
import de.henritom.actions.commands.impl.action.edit.trigger.edit.TriggerEditCommand
import de.henritom.actions.commands.impl.action.edit.trigger.remove.TriggerRemoveCommand
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object TriggerCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("trigger")
            .then(TriggerAddCommand.register())
            .then(TriggerRemoveCommand.register())
            .then(TriggerEditCommand.register())
    }
}