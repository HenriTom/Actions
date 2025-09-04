package me.henritom.actionsv2.util

import net.minecraft.client.util.InputUtil
import java.awt.event.KeyEvent
import java.util.*

object KeyUtil {
    fun parseKeyString(keyStr: String): Int {
        if (keyStr.startsWith("digit.") || keyStr.startsWith("number.") || keyStr.startsWith("raw.") || keyStr.startsWith("direct."))
            return keyStr.split(".")[1].toIntOrNull() ?: 0

        if (keyStr.startsWith("key.keyboard.", ignoreCase = true) || keyStr.startsWith("key.mouse.", ignoreCase = true) || keyStr.startsWith("scancode.", ignoreCase = true))
            return InputUtil.fromTranslationKey(keyStr.lowercase(Locale.getDefault())).code

        if (keyStr.length != 1)
            return InputUtil.fromTranslationKey("key.keyboard." + keyStr.lowercase(Locale.getDefault())).code

        return KeyEvent.getExtendedKeyCodeForChar(keyStr[0].code)
    }
}