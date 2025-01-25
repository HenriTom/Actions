package de.henritom.actions.commands.impl.action.enable

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.actions.ActionEditManager
import de.henritom.actions.config.ConfigManager
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.fabricmc.loader.api.FabricLoader

object EnableCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("enable")
            .then(ClientCommandManager.argument("name", StringArgumentType.string())
                .suggests { _, builder ->
                    ConfigManager().getDisabledActions().forEach { file ->
                        builder.suggest(file.nameWithoutExtension)
                    }
                    builder.buildFuture()
                }
                .executes { context ->
                    val name = StringArgumentType.getString(context, "name")

                    val file = FabricLoader.getInstance().configDir.toFile().resolve("actions/actions/$name.disabled")

                    if (ActionEditManager.instance.enableAction(file)) {
                        MessageUtil().printTranslatable("actions.action.enabled", name)
                        ConfigManager().reloadActions()
                        MessageUtil().printTranslatable("actions.file.reloaded.actions")
                    } else
                        MessageUtil().printTranslatable("actions.action.not_enabled", name)

                    Command.SINGLE_SUCCESS
                }
            )
    }
}