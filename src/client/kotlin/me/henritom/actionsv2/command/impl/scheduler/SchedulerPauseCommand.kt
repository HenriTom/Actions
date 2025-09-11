package me.henritom.actionsv2.command.impl.scheduler

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.axn.ActionManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object SchedulerPauseCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("pause")
            .then(
                ClientCommandManager.argument("actionID", StringArgumentType.string())
                    .executes { context ->
                        val actionID = StringArgumentType.getString(context, "actionID")
                        val action = ActionManager.loadedActions[actionID]

                        if (action == null) {
                            context.source.sendError(Text.translatable("actions.commands.about.action_not_found", actionID))
                            return@executes 0
                        }

                        if (action.scheduler == null) {
                            context.source.sendError(Text.translatable("actions.commands.scheduler.no_scheduler", actionID))
                            return@executes 0
                        }

                        if (action.scheduler!!.running) {
                            action.scheduler!!.pause()
                            context.source.sendFeedback(Text.translatable("actions.commands.scheduler.pause", actionID))
                        } else {
                            action.scheduler!!.resume()
                            context.source.sendFeedback(Text.translatable("actions.commands.scheduler.resume", actionID))
                        }

                        1
                    }
            )
    }
}