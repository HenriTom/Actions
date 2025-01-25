package de.henritom.actions.commands.impl.action.disable

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.actions.ActionEditManager
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.config.ConfigManager
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object DisableCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("disable")
            .then(ClientCommandManager.argument("name/id", StringArgumentType.string())
                .suggests { _, builder ->
                    ActionManager.instance.actions.forEach { action ->
                        builder.suggest(action.name)
                    }
                    builder.buildFuture()
                }
                .executes { context ->
                    val nameID = StringArgumentType.getString(context, "name/id")
                    val action = ActionManager.instance.getActionByNameID(nameID)

                    if (action == null) {
                        MessageUtil().printTranslatable("actions.action.not_found", nameID)
                        return@executes Command.SINGLE_SUCCESS
                    }

                    if (ActionEditManager.instance.disableAction(action)) {
                        MessageUtil().printTranslatable("actions.action.disabled", action.name)
                        ConfigManager().reloadActions()
                    } else
                        MessageUtil().printTranslatable("actions.action.not_disabled", action.name)

                    Command.SINGLE_SUCCESS
                }
            )
    }
}