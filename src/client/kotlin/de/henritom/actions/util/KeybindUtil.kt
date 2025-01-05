package de.henritom.actions.util

import java.awt.event.KeyEvent

class KeybindUtil {

    fun getKeyCodeByString(s: String): Int {
        if (s.length != 1)
            return 0

        return KeyEvent.getExtendedKeyCodeForChar(s[0].code)
    }
}