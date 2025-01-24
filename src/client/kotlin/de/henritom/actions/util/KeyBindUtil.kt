package de.henritom.actions.util

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW
import ui.MainScreen
import java.awt.event.KeyEvent
import java.util.*

class KeyBindUtil {

    fun getKeyCodeByString(s: String): Int {
        if (s.startsWith("key.keyboard.", ignoreCase = true))
            return InputUtil.fromTranslationKey(s.lowercase(Locale.getDefault())).code

        if (s.length != 1)
            return 0

        return KeyEvent.getExtendedKeyCodeForChar(s[0].code)
    }

    fun registerKeyBinds() {
        val openGUI = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "actions.options.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "actions.options.category"
            )
        )

        ClientTickEvents.END_CLIENT_TICK.register { client ->
            if (openGUI.wasPressed())
                client.setScreen(MainScreen())
        }
    }
}