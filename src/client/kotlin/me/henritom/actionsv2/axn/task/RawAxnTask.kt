package me.henritom.actionsv2.axn.task

import me.henritom.actionsv2.axn.task.impl.*
import org.apache.logging.log4j.LogManager

data class RawAxnTask(
    val type: String,
    val data: MutableMap<String, Any> = mutableMapOf()
)

fun RawAxnTask.toAxnTask(): AxnTask? {
    return when (type) {
        "log" -> LogTask().apply { data.putAll(this@toAxnTask.data) }
        "wait" -> WaitTask().apply { data.putAll(this@toAxnTask.data) }
        "if" -> IfTask().apply { data.putAll(this@toAxnTask.data) }
        "else" -> ElseTask().apply { data.putAll(this@toAxnTask.data) }
        "for" -> ForTask().apply { data.putAll(this@toAxnTask.data) }
        "while" -> WhileTask().apply { data.putAll(this@toAxnTask.data) }
        else -> {
            LogManager.getLogger("Actions/RawAxnTask").error("Unknown task type: $type")
            null
        }
    }
}
