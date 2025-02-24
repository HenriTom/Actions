package de.henritom.actions.util

import net.minecraft.client.MinecraftClient
import net.minecraft.registry.Registries
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Identifier

class SoundUtil {
    companion object {
        fun playSound(soundName: String) {
            val player = MinecraftClient.getInstance().player ?: return
            val soundId = Identifier.of(soundName)
            val soundEvent = Registries.SOUND_EVENT.get(soundId) ?: return
            player.playSound(soundEvent, 1.0f, 1.0f)
        }
    }
}