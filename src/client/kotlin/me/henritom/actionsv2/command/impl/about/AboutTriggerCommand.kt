package me.henritom.actionsv2.command.impl.about

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.axn.trigger.TriggerRegistry
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object AboutTriggerCommand {
    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("trigger")
            .then(ClientCommandManager.argument("trigger", StringArgumentType.string())
                .suggests { _, builder ->
                    for (trigger in TriggerRegistry.registeredTriggerTypes)
                        builder.suggest(trigger.type)

                    builder.buildFuture()
                }
                .executes { context ->
                    val triggerName = StringArgumentType.getString(context, "trigger")
                    val trigger = TriggerRegistry.registeredTriggerTypes.firstOrNull { it.type == triggerName }

                    if (trigger == null) {
                        context.source.sendError(Text.translatable("actions.commands.about.trigger_not_found", triggerName))
                        return@executes 0
                    }

                    context.source.sendFeedback(Text.translatable("actions.commands.about.trigger.start", trigger.type, trigger.description))

                    if (trigger.requiredData.isEmpty())
                        context.source.sendFeedback(Text.translatable("actions.commands.about.trigger.no_data", triggerName))
                    else
                        for (setting in trigger.requiredData)
                            context.source.sendFeedback(Text.translatable("actions.commands.about.trigger.setting_it", setting.name, setting.description, setting.required, setting.type, setting.defaultValue))

                    context.source.sendFeedback(Text.translatable("actions.commands.about.trigger.end"))

                    1
                }
            )
    }
}