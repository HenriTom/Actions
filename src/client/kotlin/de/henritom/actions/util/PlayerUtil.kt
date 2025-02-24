package de.henritom.actions.util

import net.minecraft.client.MinecraftClient

class PlayerUtil {
    companion object {
        fun setHotbarSlot(slot: Int) {
            if (slot in 0..8)
                MinecraftClient.getInstance().player?.inventory?.selectedSlot = slot
        }
    }
}