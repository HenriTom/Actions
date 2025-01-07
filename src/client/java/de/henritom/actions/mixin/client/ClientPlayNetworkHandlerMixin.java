package de.henritom.actions.mixin.client;

import de.henritom.actions.triggers.impl.ReceiveMessageTrigger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Unique
    private final ReceiveMessageTrigger receiveMessageTrigger = new ReceiveMessageTrigger();

    @Inject(method = "onGameMessage", at = @At("HEAD"))
    private void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        receiveMessageTrigger.trigger(String.valueOf(packet.content()));

    }

    @Inject(method = "onChatMessage", at = @At("HEAD"))
    private void onChatMessage(ChatMessageS2CPacket packet, CallbackInfo ci) {
        if (packet.sender() != null && !packet.sender().toString().equals(MinecraftClient.getInstance().getSession().getUuidOrNull().toString()))
            receiveMessageTrigger.trigger(String.valueOf(packet.unsignedContent()));
    }
}
