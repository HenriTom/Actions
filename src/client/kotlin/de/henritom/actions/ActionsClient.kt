package de.henritom.actions

import de.henritom.actions.commands.ActionsCommand
import net.fabricmc.api.ClientModInitializer

object ActionsClient : ClientModInitializer {
	override fun onInitializeClient() {
		ActionsCommand.register()
	}
}