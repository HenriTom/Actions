package de.henritom.actions

import de.henritom.actions.commands.ActionsCommand
import de.henritom.actions.config.ConfigManager
import de.henritom.actions.event.impl.ChatEvent
import de.henritom.actions.triggers.impl.*
import de.henritom.actions.util.KeyBindUtil
import de.henritom.actions.variables.GlobalVariableStorage
import de.henritom.actions.variables.LocalVariableStorage
import net.fabricmc.api.ClientModInitializer

object ActionsClient : ClientModInitializer {

	private val configManager = ConfigManager()
	val globalVariableStorage = GlobalVariableStorage()
	val localVariableStorage = LocalVariableStorage()

	override fun onInitializeClient() {
		ActionsCommand.register()

		ChatEvent.register()
		ConnectionTriggers.register()
		InvUpdateTrigger.reg()
		RegionTriggers.register()
		BiomeTriggers.register()
		PlayerStatTriggers.register()

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