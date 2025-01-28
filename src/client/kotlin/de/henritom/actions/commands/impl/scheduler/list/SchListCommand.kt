package de.henritom.actions.commands.impl.scheduler.list

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.scheduler.ActionScheduler
import de.henritom.actions.scheduler.SchedulerHelper
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object SchListCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("list")
            .executes {
                MessageUtil().printTranslatable("actions.scheduler.list.title", ActionScheduler.runningActions.size.toString())

                for (scheduler in ActionScheduler.runningActions)
                    MessageUtil().printTranslatable("actions.list.it", scheduler.action.name, scheduler.runID.toString())

                Command.SINGLE_SUCCESS
            }
    }
}