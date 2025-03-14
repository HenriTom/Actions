package de.henritom.actions

import de.henritom.actions.commands.ActionsCommand
import de.henritom.actions.config.ConfigManager
import de.henritom.actions.event.impl.ChatEvent
import de.henritom.actions.sharing.SharingAPI
import de.henritom.actions.triggers.impl.*
import de.henritom.actions.util.KeyBindUtil
import de.henritom.actions.variables.GlobalVariableStorage
import de.henritom.actions.variables.LocalVariableStorage
import net.fabricmc.api.ClientModInitializer

object ActionsClient : ClientModInitializer {

	private val configManager = ConfigManager()

	val globalVariableStorage = GlobalVariableStorage()
	val localVariableStorage = LocalVariableStorage()

	var sharingAPI: SharingAPI? = null

	override fun onInitializeClient() {
		ActionsCommand.register()

		ChatEvent.register()
		ConnectionTriggers.register()
		InvUpdateTrigger.reg()
		RegionTriggers.register()
		BiomeTriggers.register()
		PlayerStatTriggers.register()

		sharingAPI = SharingAPI("http://api.henritom.me:8080/actions")

		Runtime.getRuntime().addShutdownHook(Thread {
			configManager.saveConfig()
			configManager.saveAllActions()
			configManager.saveRegions()
			configManager.saveLocalVariables()
		})

		configManager.loadConfig()
		configManager.loadActions()
		configManager.loadRegions()
		configManager.loadLocalVariables()

		KeyBindUtil().registerKeyBinds()
	}
}