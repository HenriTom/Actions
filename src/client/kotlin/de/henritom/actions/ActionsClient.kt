package de.henritom.actions

import de.henritom.actions.commands.ActionsCommand
import de.henritom.actions.config.ConfigManager
import de.henritom.actions.event.impl.ChatEvent
import de.henritom.actions.triggers.impl.BiomeTriggers
import de.henritom.actions.triggers.impl.ConnectionTriggers
import de.henritom.actions.triggers.impl.InvUpdateTrigger
import de.henritom.actions.triggers.impl.RegionTriggers
import de.henritom.actions.util.KeyBindUtil
import net.fabricmc.api.ClientModInitializer

object ActionsClient : ClientModInitializer {

	private val configManager = ConfigManager()

	override fun onInitializeClient() {
		ActionsCommand.register()

		ChatEvent.register()
		ConnectionTriggers.register()
		InvUpdateTrigger.reg()
		RegionTriggers.register()
		BiomeTriggers.register()

		Runtime.getRuntime().addShutdownHook(Thread {
			configManager.saveConfig()
			configManager.saveAllActions()
			configManager.saveRegions()
		})

		configManager.loadConfig()
		configManager.loadActions()
		configManager.loadRegions()

		KeyBindUtil().registerKeyBinds()
	}
}