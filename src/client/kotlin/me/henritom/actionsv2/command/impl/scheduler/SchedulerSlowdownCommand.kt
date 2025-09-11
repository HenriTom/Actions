package me.henritom.actionsv2.command.impl.scheduler

import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.axn.ActionManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object SchedulerSlowdownCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("slowdown")
            .then(ClientCommandManager.argument("actionID", StringArgumentType.string())
                .then(ClientCommandManager.argument("divider", DoubleArgumentType.doubleArg(1.0))
                    .executes { context ->
                        val actionID = StringArgumentType.getString(context, "actionID")
                        val divider = DoubleArgumentType.getDouble(context, "divider")
                        val action = ActionManager.loadedActions[actionID]

                        if (action == null) {
                            context.source.sendError(Text.translatable("actions.commands.about.action_not_found", actionID))
                            return@executes 0
                        }

                        if (action.scheduler == null) {
                            context.source.sendError(Text.translatable("actions.commands.scheduler.no_scheduler", actionID))
                            return@executes 0
                        }

                        action.scheduler!!.slowDown(divider)
                        context.source.sendFeedback(Text.translatable("actions.commands.scheduler.slowdown.success", actionID, divider))

                        1
                    }
                )
            )
    }
}