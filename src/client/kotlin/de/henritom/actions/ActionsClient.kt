package de.henritom.actions

import de.henritom.actions.commands.ActionsCommand
import de.henritom.actions.event.impl.ChatEvent
import net.fabricmc.api.ClientModInitializer

object ActionsClient : ClientModInitializer {
	override fun onInitializeClient() {
		ActionsCommand.register()
		ChatEvent.register()
	}
}