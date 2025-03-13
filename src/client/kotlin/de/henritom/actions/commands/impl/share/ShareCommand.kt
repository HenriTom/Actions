package de.henritom.actions.commands.impl.share

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.commands.impl.share.download.DownloadCommand
import de.henritom.actions.commands.impl.share.upload.UploadCommand
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object ShareCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("share")
            .then(DownloadCommand.register())
            .then(UploadCommand.register())
    }
}