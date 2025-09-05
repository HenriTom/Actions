package me.henritom.actionsv2.command.impl.scheduler

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import me.henritom.actionsv2.axn.ActionManager
import me.henritom.actionsv2.scheduler.ActionScheduler
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object SchedulerExecuteCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("execute")
            .then(ClientCommandManager.argument("actionID", StringArgumentType.string())
                .then(ClientCommandManager.argument("privilegeLevel", IntegerArgumentType.integer(0, 2))
                    .then(ClientCommandManager.argument("callArgs", StringArgumentType.string())
                        .suggests { _, builder ->
                            builder.suggest("\"\"")
                            builder.buildFuture()
                        }
                        .executes { context ->
                            trigger(context, context.getArgument("privilegeLevel", Int::class.java), StringArgumentType.getString(context, "callArgs"))

                            1
                        }
                    )
                    .suggests { _, builder ->
                        builder.suggest("0")
                        builder.suggest("1")
                        builder.suggest("2")

                        builder.buildFuture()

                    }
                    .executes { context ->
                        trigger(context, context.getArgument("privilegeLevel", Int::class.java))
                    }
                )
                .suggests { _, builder ->
                    ActionManager.loadedActions.forEach { (id, _) ->
                        builder.suggest(id)
                    }
                    builder.buildFuture()
                }
                .executes { context ->
                    trigger(context)
                }
            )
    }

    fun trigger(context: CommandContext<FabricClientCommandSource>, privilegeLevel: Int = 0, callArgs: String = ""): Int {
        val actionID = StringArgumentType.getString(context, "actionID")
        val action = ActionManager.loadedActions[actionID]

        if (action == null) {
            context.source.sendError(Text.translatable("actions.commands.about.action_not_found", actionID))
            return 0
        }

        action.variables["privilege_level"] = privilegeLevel
        action.variables["call_args"] = callArgs

        ActionScheduler(action).start()
        context.source.sendFeedback(Text.translatable("actions.commands.call.success", actionID))

        return 1
    }
}