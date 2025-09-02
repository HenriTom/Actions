package me.henritom.actionsv2.command.impl.about

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.axn.ActionManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object AboutActionCommand {
    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("action")
            .then(ClientCommandManager.argument("actionID", StringArgumentType.string())
                .suggests {  _, builder ->
                    ActionManager.loadedActions.forEach { (id, _) ->
                        builder.suggest(id)
                    }
                    builder.buildFuture()
                }
                .then(ClientCommandManager.argument("stat", StringArgumentType.string())
                    .suggests { _, builder ->
                        builder.suggest("triggers")
                        builder.suggest("tasks")
                        builder.suggest("variables")
                        builder.buildFuture()
                    }.executes { context ->
                        val actionID = StringArgumentType.getString(context, "actionID")
                        val stat = StringArgumentType.getString(context, "stat")
                        val action = ActionManager.loadedActions[actionID]

                        if (action == null) {
                            context.source.sendError(Text.translatable("actions.commands.about.action_not_found", actionID))
                            return@executes 0
                        }

                        when (stat) {
                            "triggers" -> {
                                context.source.sendFeedback(Text.translatable("actions.commands.about.triggers", action.id, action.name, action.version, action.author, action.environment, action.loaderVersion, action.triggers.size))

                                for (trigger in action.triggers)
                                    context.source.sendFeedback(Text.translatable("actions.commands.about.it", trigger.type))
                            }

                            "tasks" -> {
                                context.source.sendFeedback(Text.translatable("actions.commands.about.tasks", action.id, action.name, action.version, action.author, action.environment, action.loaderVersion, action.tasks.size))

                                for (task in action.tasks)
                                    context.source.sendFeedback(Text.translatable("actions.commands.about.it", task.type))
                            }

                            "variables" -> {
                                context.source.sendFeedback(Text.translatable("actions.commands.about.variables", action.id, action.name, action.version, action.author, action.environment, action.loaderVersion, action.variables.size))

                                for (variable in action.variables)
                                    context.source.sendFeedback(Text.translatable("actions.commands.about.it", variable.key))
                            }

                            else -> context.source.sendError(Text.translatable("actions.commands.about.no_option", stat))
                        }

                        1
                    }
                )
                .suggests { _, builder ->
                    ActionManager.loadedActions.forEach { (id, _) ->
                        builder.suggest(id)
                    }
                    builder.buildFuture()
                }
                .executes { context ->
                    val actionID = StringArgumentType.getString(context, "actionID")
                    val action = ActionManager.loadedActions[actionID]

                    if (action == null) {
                        context.source.sendError(Text.translatable("actions.commands.about.action_not_found", actionID))
                        return@executes 0
                    }

                    context.source.sendFeedback(Text.translatable("actions.commands.about.message", action.id, action.name, action.version, action.author, action.environment, action.loaderVersion, action.description, action.triggers.size, action.tasks.size, action.variables.size, action.changed, action.selected, action.hidden, action.disabled))

                    1
                }
            )
    }
}