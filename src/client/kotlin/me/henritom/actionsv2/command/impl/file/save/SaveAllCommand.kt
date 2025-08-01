package me.henritom.actionsv2.command.impl.file.save

import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.loader.ActionsLoader
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object SaveAllCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("all")
            .then(ClientCommandManager.argument("includeUnchanged", BoolArgumentType.bool())
                .executes { context ->
                    val includeUnchanged = BoolArgumentType.getBool(context, "includeUnchanged")

                    ActionsLoader.saveAll(!includeUnchanged)

                    if (includeUnchanged)
                        context.source.sendFeedback(Text.translatable("actions.commands.file.save.all_unchanged.success"))
                    else
                        context.source.sendFeedback(Text.translatable("actions.commands.file.save.all.success"))

                    1
                }
            )
            .executes { context ->
                ActionsLoader.saveAll(true)

                context.source.sendFeedback(Text.translatable("actions.commands.file.save.all.success"))

                1
            }
    }
}