package me.henritom.actionsv2.axn.trigger

import me.henritom.actionsv2.axn.AxnAction

class AxnTrigger (
    val type: String,
    val data: MutableMap<String, Any> = mutableMapOf(),
    var callArgs: String
) {
    @Transient
    var parent: AxnAction? = null

    fun trigger(): Boolean {
        return try {
            var privilegeLevel = 0

            if (data["privilege_level"] != null)
                privilegeLevel = data["privilege_level"] as Int

            parent?.execute(this, privilegeLevel, callArgs)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}