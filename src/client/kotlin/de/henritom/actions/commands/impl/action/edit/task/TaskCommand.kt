package de.henritom.actions.commands.impl.action.edit.task

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.commands.impl.action.edit.task.add.TaskAddCommand
import de.henritom.actions.commands.impl.action.edit.task.edit.TaskEditCommand
import de.henritom.actions.commands.impl.action.edit.task.remove.TaskRemoveCommand
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object TaskCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("task")
            .then(TaskAddCommand.register())
            .then(TaskRemoveCommand.register())
            .then(TaskEditCommand.register())
    }
}