package me.henritom.actionsv2

import me.henritom.actionsv2.ace.SchemaBuilder
import me.henritom.actionsv2.axn.task.TaskRegistry
import me.henritom.actionsv2.axn.trigger.TriggerRegistry
import me.henritom.actionsv2.command.ActionsCommand
import me.henritom.actionsv2.loader.ActionsLoader
import me.henritom.actionsv2.regions.RegionManager
import me.henritom.actionsv2.util.SessionUtil
import me.henritom.actionsv2.variables.GlobalVariableStorage
import me.henritom.actionsv2.variables.LocalVariableStorage
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents

object ActionsV2Client : ClientModInitializer {
	override fun onInitializeClient() {
		GlobalVariableStorage.init()
		LocalVariableStorage.init()

		RegionManager.init()
        SessionUtil.loadSession()

		TaskRegistry.initTypes()
		TriggerRegistry.initTypes()

		ActionsCommand.register()

		ActionsLoader.loadAll()

		ClientLifecycleEvents.CLIENT_STOPPING.register {
			ActionsLoader.saveAll()
            SessionUtil.saveSession()
		}

		SchemaBuilder.build()
	}
}