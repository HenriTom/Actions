package me.henritom.actionsv2.command.impl.debug

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.util.KeyUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object KeyConvertCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("key_convert")
            .then(ClientCommandManager.argument("keyStr", StringArgumentType.string())
                .executes { context ->
                    val keyStr = StringArgumentType.getString(context, "keyStr")

                    context.source.sendFeedback(Text.translatable("actions.commands.debug.time_convert.success", keyStr, KeyUtil.parseKeyString(keyStr)))

                    1
                }
            )
    }
}