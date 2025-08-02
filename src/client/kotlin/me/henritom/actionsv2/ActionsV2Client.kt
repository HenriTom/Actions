package me.henritom.actionsv2

import me.henritom.actionsv2.axn.task.TaskRegistry
import me.henritom.actionsv2.axn.trigger.TriggerRegistry
import me.henritom.actionsv2.command.ActionsCommand
import me.henritom.actionsv2.loader.ActionsLoader
import me.henritom.actionsv2.variables.GlobalVariableStorage
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents

object ActionsV2Client : ClientModInitializer {
	override fun onInitializeClient() {
		GlobalVariableStorage.init()

		TaskRegistry.initTypes()
		TriggerRegistry.initTypes()

		ActionsCommand.register()

		ActionsLoader.loadAll()

		ClientLifecycleEvents.CLIENT_STOPPING.register {
			ActionsLoader.saveAll()
		}
	}
}