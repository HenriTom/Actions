package de.henritom.actions.commands.impl.scheduler

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.commands.impl.scheduler.end.EndCommand
import de.henritom.actions.commands.impl.scheduler.endall.EndAllCommand
import de.henritom.actions.commands.impl.scheduler.list.SchListCommand
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object SchedulerCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("scheduler")
            .then(EndCommand.register())
            .then(EndAllCommand.register())
            .then(SchListCommand.register())
    }
}