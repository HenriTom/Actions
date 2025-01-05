package de.henritom.actions.event.impl

import com.mojang.authlib.GameProfile
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.triggers.impl.CommandTrigger
import de.henritom.actions.triggers.impl.ReceiveMessageTrigger
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.network.message.MessageType
import net.minecraft.network.message.SignedMessage
import net.minecraft.text.Text
import java.time.Instant

class ChatEvent {
    companion object {
        fun register() {
            ClientSendMessageEvents.ALLOW_CHAT.register(ClientSendMessageEvents.AllowChat { message ->
                if (ActionManager.instance.commandPrefix.isEmpty())
                    return@AllowChat true

                if (message.startsWith(ActionManager.instance.commandPrefix)) {
                    CommandTrigger().trigger(message)
                    false
                } else
                    true
            })

            ClientReceiveMessageEvents.ALLOW_CHAT.register { message: Text, _: SignedMessage?, _: GameProfile?, _: MessageType.Parameters, _: Instant ->
                if (message.string.split("> ")[0].split("<")[1] != MinecraftClient.getInstance().session.username)
                    ReceiveMessageTrigger().trigger(message.string.split("> ")[1])

                true
            }
        }
    }
}