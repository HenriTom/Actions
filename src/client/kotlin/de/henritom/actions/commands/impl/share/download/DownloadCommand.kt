package de.henritom.actions.commands.impl.share.download

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.ActionsClient
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import java.util.concurrent.CompletableFuture

object DownloadCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("download")
            .then(ClientCommandManager.argument("id", StringArgumentType.string())
                .executes { context ->
                    val id = StringArgumentType.getString(context, "id")

                    val messageUtil = MessageUtil(null)

                    if (id.length != 6) {
                        messageUtil.printTranslatable("actions.shareid.not_found", id)
                        return@executes Command.SINGLE_SUCCESS
                    }

                    val future = ActionsClient.sharingAPI?.downloadActionInBackground(id.uppercase()) ?: CompletableFuture.completedFuture("null")

                    if (!future.get().equals("null") && future.get().endsWith(".json")) {
                        val action = ActionManager.instance.getActionByNameID(future.get().split(".json")[0])

                        if (action == null) {
                            println("null: ${future.get()}")
                            messageUtil.printTranslatable("actions.shareid.not_found", id)
                            return@executes Command.SINGLE_SUCCESS
                        }

                        messageUtil.printTranslatable("actions.action.downloaded", action.name, id)
                    } else
                        messageUtil.printTranslatable("actions.shareid.not_found", id)

                    Command.SINGLE_SUCCESS
                })
    }
}