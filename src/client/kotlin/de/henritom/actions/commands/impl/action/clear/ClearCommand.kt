package de.henritom.actions.commands.impl.action.clear

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.config.ConfigManager
import de.henritom.actions.triggers.TriggerEnum
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object ClearCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("clear")
            .then(ClientCommandManager.argument("name/id", StringArgumentType.string())
                .suggests { _, builder ->
                    ActionManager.instance.actions.filter { action ->
                        for (trigger in action.triggers)
                            if (trigger.type == TriggerEnum.CALL)
                                return@filter true
                        false
                    }.forEach { action ->
                        builder.suggest(action.name)
                    }
                    builder.buildFuture()
                }
                .executes { context ->
                    val nameID = StringArgumentType.getString(context, "name/id")
                    val action = ActionManager.instance.getActionByNameID(nameID)

                    if (action != null) {
                        action.tasks.clear()
                        action.triggers.clear()
                        ConfigManager().saveAction(action)

                        MessageUtil().printTranslatable("actions.action.cleared", nameID)
                    } else
                        MessageUtil().printTranslatable("actions.action.not_found", nameID)

                    Command.SINGLE_SUCCESS
                }
            )
    }
}