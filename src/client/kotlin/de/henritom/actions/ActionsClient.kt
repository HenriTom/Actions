package de.henritom.actions

import de.henritom.actions.commands.ActionsCommand
import de.henritom.actions.config.ConfigManager
import de.henritom.actions.event.impl.ChatEvent
import de.henritom.actions.triggers.impl.ConnectionTriggers
import de.henritom.actions.util.KeyBindUtil
import net.fabricmc.api.ClientModInitializer

object ActionsClient : ClientModInitializer {

	override fun onInitializeClient() {
		ActionsCommand.register()

		ChatEvent.register()
		ConnectionTriggers.register()

		Runtime.getRuntime().addShutdownHook(Thread {
			ConfigManager().saveConfig()
			ConfigManager().saveAllActions()
		})

		ConfigManager().loadConfig()
		ConfigManager().loadActions()

		KeyBindUtil().registerKeyBinds()
	}
}