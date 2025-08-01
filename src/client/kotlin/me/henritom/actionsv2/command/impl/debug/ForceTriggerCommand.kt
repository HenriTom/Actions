package me.henritom.actionsv2.command.impl.debug

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.axn.trigger.TriggerRegistry
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object ForceTriggerCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("forcetrigger")
            .then(ClientCommandManager.argument("trigger", StringArgumentType.string())
                .executes { context ->
                    val triggerName = StringArgumentType.getString(context, "trigger")

                    TriggerRegistry.triggerAll(triggerName)

                    context.source.sendFeedback(Text.translatable("actions.commands.debug.forcetrigger.success", triggerName))

                    1
                }
            )
    }
}