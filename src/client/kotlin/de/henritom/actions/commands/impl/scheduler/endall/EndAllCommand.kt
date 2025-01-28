package de.henritom.actions.commands.impl.scheduler.endall

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.scheduler.SchedulerHelper
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object EndAllCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("endall")
            .executes {
                SchedulerHelper().endAllActions()
                MessageUtil().printTranslatable("actions.scheduler.end_all")

                Command.SINGLE_SUCCESS
            }
    }
}