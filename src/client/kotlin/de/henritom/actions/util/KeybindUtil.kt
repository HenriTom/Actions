package de.henritom.actions.util

import net.minecraft.client.util.InputUtil
import java.awt.event.KeyEvent
import java.util.*

class KeybindUtil {

    fun getKeyCodeByString(s: String): Int {
        if (s.startsWith("key.keyboard.", ignoreCase = true))
            return InputUtil.fromTranslationKey(s.lowercase(Locale.getDefault())).code

        if (s.length != 1)
            return 0

        return KeyEvent.getExtendedKeyCodeForChar(s[0].code)
    }
}