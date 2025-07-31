package me.henritom.actionsv2.axn

import me.henritom.actionsv2.axn.task.AxnTask
import me.henritom.actionsv2.axn.trigger.AxnTrigger
import me.henritom.actionsv2.scheduler.ActionScheduler
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class AxnAction(

    // Metadata
    val loaderVersion: Int, // Version of the loader that created this action
    val environment: String, // Environment this action is intended for (e.g., "client", "server")

    @Transient
    var changed: Boolean, // Whether the action has been changed since it was loaded

    @Transient
    var selected: Boolean, // Whether the action is currently selected in the UI

    var hidden: Boolean, // Whether the action is hidden in the UI
    var disabled: Boolean, // Whether the action is disabled from being run

    // Action data
    val name: String, // Name of the action
    val id: String, // Unique identifier for the action
    val version: String, // Version of the action
    val author: String, // Author of the action
    val description: String, // Description of the action

    // Action components
    val triggers: List<AxnTrigger>, // List of triggers that can start the action
    val tasks: List<AxnTask>, // List of tasks that the action performs

    val variables: MutableMap<String, Any> // Map of variables usable in the action
) {

    @Transient
    val logger: Logger = LogManager.getLogger("Actions/Action/${id}")

    @Transient
    var scheduler: ActionScheduler? = null
        private set

    @Transient
    var privilegeLevel: Int = 0 // Privilege level the action uses while running (0 = Basic, 1 = Elevated, 2 = Privileged)
        private set

    fun execute(trigger: AxnTrigger, privilegeLevel: Int = 0): ActionScheduler? {
        if (disabled) {
            logger.warn("Action $id is disabled.")
            return null
        }

        if (!triggers.contains(trigger)) {
            logger.warn("Trigger ${trigger.type} is not valid for this action.")
            return null
        }

        require(privilegeLevel in 0..2) { "Invalid privilege level: $privilegeLevel" }
        this.privilegeLevel = privilegeLevel

        scheduler = ActionScheduler(this).start()

        return scheduler!!
    }

    fun complete() {
        privilegeLevel = 0
        scheduler?.takeIf { it.isAlive }?.kill()
        scheduler = null
    }
}
