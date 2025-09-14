package me.henritom.actionsv2.command.impl.debug

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.util.FunctionUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object FunctionConvertCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("function_convert")
            .then(ClientCommandManager.argument("function", StringArgumentType.string())
                .executes { context ->
                    val function = StringArgumentType.getString(context, "function")

                    context.source.sendFeedback(Text.translatable("actions.commands.debug.time_convert.success", function, FunctionUtil.functionStringToBoolean(function)))

                    1
                }
            )
    }
}