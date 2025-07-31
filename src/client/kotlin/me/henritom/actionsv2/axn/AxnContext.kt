package me.henritom.actionsv2.axn

import me.henritom.actionsv2.scheduler.ActionScheduler
import net.minecraft.client.MinecraftClient
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class AxnContext(
    val action: AxnAction,
    val scheduler: ActionScheduler,
    val variables: MutableMap<String, Any> = action.variables,
    val logger: Logger = LogManager.getLogger("Actions/Context/${action.id}"),
    val client: MinecraftClient = MinecraftClient.getInstance()
) {
    fun getVariable(name: String): Any? = variables[name]
    fun setVariable(name: String, value: Any) { variables[name] = value }
}