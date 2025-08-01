package me.henritom.actionsv2.axn

import me.henritom.actionsv2.axn.task.RawAxnTask
import me.henritom.actionsv2.axn.task.TaskRegistry
import me.henritom.actionsv2.axn.task.toAxnTask
import me.henritom.actionsv2.axn.trigger.AxnTrigger
import me.henritom.actionsv2.axn.trigger.TriggerRegistry

data class RawAxnAction(
    val loaderVersion: Int,
    val environment: String,
    val hidden: Boolean,
    val disabled: Boolean,

    val name: String,
    var id: String,
    val version: String,
    val author: String,
    val description: String,

    val triggers: List<AxnTrigger>,
    val tasks: List<RawAxnTask>,

    val variables: MutableMap<String, Any>
)

fun RawAxnAction.toAxnAction(): AxnAction {
    val action = AxnAction(
        loaderVersion = loaderVersion,
        environment = environment,
        changed = false,
        selected = false,
        hidden = hidden,
        disabled = disabled,
        name = name,
        id = id,
        version = version,
        author = author,
        description = description,
        triggers = triggers,
        tasks = tasks.mapNotNull { it.toAxnTask() },
        variables = variables
    )

    action.triggers.forEach {
        it.parent = action
        TriggerRegistry.registerTrigger(it)
    }

    action.tasks.forEach {
        TaskRegistry.registerTask(it)
    }

    return action
}
