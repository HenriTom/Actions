package de.henritom.actions.commands.impl.share.upload

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.ActionsClient
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import java.util.concurrent.CompletableFuture

object UploadCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("upload")
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

                    val messageUtil = MessageUtil(null)

                    val future = ActionsClient.sharingAPI?.uploadActionInBackground(action) ?: CompletableFuture.completedFuture("null")
                    if (!future.get().equals("null"))
                        messageUtil.printTranslatable("actions.action.shared", action!!.name, future.get())
                    else
                        messageUtil.printTranslatable("actions.action.not_found", nameID)

                    Command.SINGLE_SUCCESS
                })
    }
}