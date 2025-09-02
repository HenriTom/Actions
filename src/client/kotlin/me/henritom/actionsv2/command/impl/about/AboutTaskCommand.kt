package me.henritom.actionsv2.command.impl.about

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.axn.task.TaskRegistry
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object AboutTaskCommand {
    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("task")
            .then(ClientCommandManager.argument("task", StringArgumentType.string())
                .suggests { _, builder ->
                    for (task in TaskRegistry.registeredTaskTypes)
                        builder.suggest(task.type)

                    builder.buildFuture()
                }
                .executes { context ->
                    val taskName = StringArgumentType.getString(context, "task")
                    val task = TaskRegistry.registeredTaskTypes.firstOrNull { it.type == taskName }

                    if (task == null) {
                        context.source.sendError(Text.translatable("actions.commands.about.task_not_found", taskName))
                        return@executes 0
                    }

                    context.source.sendFeedback(Text.translatable("actions.commands.about.task.start", task.type, task.description))

                    if (task.requiredData.isEmpty())
                        context.source.sendFeedback(Text.translatable("actions.commands.about.trigger.no_data", taskName))
                    else
                        for (setting in task.requiredData)
                            context.source.sendFeedback(Text.translatable("actions.commands.about.trigger.setting_it", setting.name, setting.description, setting.required, setting.type, setting.defaultValue))

                    context.source.sendFeedback(Text.translatable("actions.commands.about.trigger.end"))

                    1
                }
            )
    }
}