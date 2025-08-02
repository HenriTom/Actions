package me.henritom.actionsv2.command.impl.debug

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.axn.trigger.TriggerRegistry
import me.henritom.actionsv2.util.TimeUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object TimeConvertCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("time_convert")
            .then(ClientCommandManager.argument("timeStr", StringArgumentType.string())
                .executes { context ->
                    val timeStr = StringArgumentType.getString(context, "timeStr")

                    TriggerRegistry.triggerAll(timeStr)

                    context.source.sendFeedback(Text.translatable("actions.commands.debug.time_convert.success", timeStr, TimeUtil.parseTimeString(timeStr)))

                    1
                }
            )
    }
}