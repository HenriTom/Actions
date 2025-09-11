package me.henritom.actionsv2.command.impl.scheduler

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.axn.ActionManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object SchedulerJumpCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("jump")
            .then(ClientCommandManager.argument("actionID", StringArgumentType.string())
                .then(ClientCommandManager.argument("taskIndex", IntegerArgumentType.integer(1))
                    .executes { context ->
                        val actionID = StringArgumentType.getString(context, "actionID")
                        val taskIndex = IntegerArgumentType.getInteger(context, "taskIndex")
                        val action = ActionManager.loadedActions[actionID]

                        if (action == null) {
                            context.source.sendError(Text.translatable("actions.commands.about.action_not_found", actionID))
                            return@executes 0
                        }

                        if (action.scheduler == null) {
                            context.source.sendError(Text.translatable("actions.commands.scheduler.no_scheduler", actionID))
                            return@executes 0
                        }

                        if (action.scheduler!!.jumpTo(taskIndex - 1)) {
                            context.source.sendFeedback(Text.translatable("actions.commands.scheduler.jump.success", taskIndex, actionID))
                            1
                        } else {
                            context.source.sendError(Text.translatable("actions.commands.scheduler.jump.too_high", taskIndex, actionID))
                            0
                        }
                    }
                )
            )
    }
}