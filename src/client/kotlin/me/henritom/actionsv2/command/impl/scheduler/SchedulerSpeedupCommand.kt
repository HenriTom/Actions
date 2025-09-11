package me.henritom.actionsv2.command.impl.scheduler

import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.axn.ActionManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object SchedulerSpeedupCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("speedup")
            .then(ClientCommandManager.argument("actionID", StringArgumentType.string())
                .then(ClientCommandManager.argument("multiplier", DoubleArgumentType.doubleArg(1.0))
                    .executes { context ->
                        val actionID = StringArgumentType.getString(context, "actionID")
                        val multiplier = DoubleArgumentType.getDouble(context, "multiplier")
                        val action = ActionManager.loadedActions[actionID]

                        if (action == null) {
                            context.source.sendError(Text.translatable("actions.commands.about.action_not_found", actionID))
                            return@executes 0
                        }

                        if (action.scheduler == null) {
                            context.source.sendError(Text.translatable("actions.commands.scheduler.no_scheduler", actionID))
                            return@executes 0
                        }

                        action.scheduler!!.speedUp(multiplier)
                        context.source.sendFeedback(Text.translatable("actions.commands.scheduler.speedup.success", actionID, multiplier))

                        1
                    }
                )
            )
    }
}