package me.henritom.actionsv2

import me.henritom.actionsv2.axn.task.TaskRegistry
import me.henritom.actionsv2.axn.trigger.TriggerRegistry
import me.henritom.actionsv2.loader.ActionsLoader
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents

object ActionsV2Client : ClientModInitializer {
	override fun onInitializeClient() {
		TaskRegistry.initTypes()
		TriggerRegistry.initTypes()

		val loader = ActionsLoader()
		loader.loadAll()

		ClientLifecycleEvents.CLIENT_STOPPING.register {
			loader.saveAll()
		}

		TriggerRegistry.triggerAll("test_trigger")
	}
}