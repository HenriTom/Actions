package me.henritom.actionsv2.command.impl

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.command.impl.scheduler.*
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object SchedulerCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("scheduler")
            .then(SchedulerExecuteCommand.register())
            .then(SchedulerKillCommand.register())
            .then(SchedulerPauseCommand.register())
            .then(SchedulerSlowdownCommand.register())
            .then(SchedulerSpeedupCommand.register())
    }
}