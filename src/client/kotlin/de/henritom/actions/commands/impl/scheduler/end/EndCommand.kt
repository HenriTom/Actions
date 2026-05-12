package de.henritom.actions.commands.impl.scheduler.end

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.scheduler.ActionScheduler
import de.henritom.actions.scheduler.SchedulerHelper
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object EndCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("end")
            .then(ClientCommandManager.argument("runID", IntegerArgumentType.integer(0))
                .suggests { _, builder ->
                    ActionScheduler.runningActions.forEach { scheduler ->
                        builder.suggest(scheduler.runID)
                    }
                    builder.buildFuture()
                }
                .executes { context ->
                    val runID = IntegerArgumentType.getInteger(context, "runID")
                    val scheduler = SchedulerHelper().getSchedulerByRunID(runID)

                    val messageUtil = MessageUtil(null)

                    SchedulerHelper().getSchedulerByRunID(runID)?.end()
                    if (scheduler != null)
                        messageUtil.printTranslatable("actions.scheduler.ended", scheduler.action.name, scheduler.runID.toString())
                    else
                        messageUtil.printTranslatable("actions.scheduler.not_ended", runID.toString())

                    Command.SINGLE_SUCCESS
                })
    }

    fun register2(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("endall")
            .then(ClientCommandManager.argument("actionID", StringArgumentType.string())
                .suggests { _, builder ->
                    ActionScheduler.runningActions
                        .distinctBy { it.action.id }
                        .forEach { scheduler ->
                            builder.suggest(scheduler.action.id.toString())
                        }
                    builder.buildFuture()
                }
                .executes { context ->
                    val actionId = StringArgumentType.getString(context, "actionID")
                    val schedulers = SchedulerHelper().getSchedulersByActionId(actionId)

                    val messageUtil = MessageUtil(null)

                    if (schedulers.isEmpty()) {
                        messageUtil.printTranslatable("actions.scheduler.not_ended", actionId)
                        return@executes Command.SINGLE_SUCCESS
                    }

                    for (scheduler in schedulers) {
                        scheduler.end()

                        messageUtil.printTranslatable("actions.scheduler.ended", scheduler.action.name, scheduler.runID.toString())
                    }

                    Command.SINGLE_SUCCESS
                })
    }
}